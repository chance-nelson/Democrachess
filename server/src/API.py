'''
Main file for Democrachess API

Handles all requests for getting the game state, keeps track of voting and
moves, and authenticates players and alters player statistics
'''

from flask import Flask
api = Flask(__name__)
from api import views
from api import api
import JWT
import json
from pymongo import MongoClient
import bcrypt
import chess
import threading

# Initialize JWT utilities
secret = 'secret'  # Get the secret
jwt = JWT()        # Init JWT encoder/decoder

# Initialize MongoDB utilities
mongo = MongoClient('localhost', 27017)  # Connect to the database
db = mongo.democrachess                  # Get the democrachess database
users = db.users                         # Get the users collection

# Initialize chess helper variables
board = chess.Board()    # Initialize chess board
voters = []              # Array of who has voted for the current move
votersCurrentMatch = []  # Array of all voters over the course of the match
votes = {}               # Dictionary of all voted moves {'a1b2': 14, ...}

# Initialize the threading timer for making moves, checking for checkmates, etc
t = threading.Timer(60.0, make_move)

def check_valid_jwt(jwt):
    '''Check the validity of a JSON Web Token
    Args:
        jwt (string): JSON Web Token string
    Returns:
        If the JWT is successfully decoded, the username of the JWT is
        returned. Otherwise, False is returned.
    '''
    with decoded as jwt.decode(jwt, secret, algorithms=['HS256']):
        if not decoded:
            return False
        else:
            return decoded['sub']

@api.route('/register', methods=['POST'])
def register():
    '''Register a new user
    Returns:
        If there are no duplicate usernames in the database, a 200
        application/json response is sent with a valid JSON Web Token in the
        body. Otherwise, an empty 406 Not Acceptable response is sent.
    '''
    # Get the username and password from the request body
    username = request.get_json()['username']
    password = request.get_json()['password']
    team = request.get_json()['team']

    # Search the users collection for a duplicate username
    user = users.find_one({'username': username})

    # If a duplicate user exists, send a 406 error
    if user:
        return make_response(406)
    # Else, create a new entry in the database for the user, and generate a JWT
    else:
        users.insert_one({'username': username,
                          'password': bcrypt.hashpw(password,
                                                    bcrypt.gensalt()),
                          'wins': 0,
                          'losses': 0,
                          'team': team})
 
        # Set up the payload, with issuer, and username
        payload = {
                    'iss': 'http://www.vxhvx.com/democrachess'
                    'sub': username
                  }
        
        jws = jwt.encode(payload, secret, algorithm='HS256')  # Encode the JWT

        return make_response(jsonify({'jwt': jws}), 200)      # Send it off

@api.route('/auth', methods=['POST'])
def auth():
    '''Authenticate a user with the API, checks username and password
    Returns:
        If the username and hashed password match an entry in the database,
        a 200 application/json response is sent with a valid JSON Web Token 
        in the body. Otherwise, a 401 response is sent.
    '''
    # First, get the username and password from the request body
    username = request.get_json()['username']
    password = request.get_json()['password']
    
    # Search the users collection for the user attempting to login
    user = users.find_one({'username': username})

    # If the user does not exist, return 401
    if not user:
        return make_response(401)

    # If there is no user/password combination found in the collection, return
    # a 401 error
    if not bcrypt.checkpw(password, user['password']):
        return make_response(401)
    else:
        # Set up the payload, with issuer, and username
        payload = {
                    'iss': 'http://www.vxhvx.com/democrachess'
                    'sub': username
                  }
        
        jws = jwt.encode(payload, secret, algorithm='HS256')  # Encode the JWT

        return make_response(jsonify({'jwt': jws}), 200)      # Send it off

@api.route('/game', methods=['GET'])
def send_game_state():
    '''Send the game state to a client
    Returns:
        If a valid JWT is found in the headers, a 200 application/json
        response is sent containg a JSON object containing a FEN game state
        string, and a dictionary of voters
    '''
    if not check_valid_jwt('Authorization')[5::]:
        return make_response(401)

    return make_response(jsonify({'state': board.fen(),
                                  'votes': votes}),
                         200)

@api.route('/game', methods=['POST'])
def get_move_vote():
    '''Recieve and process a move vote
    Returns:
        If the JWT recieved is valid, and the move sent is a legal move, the
        vote is added to votes{}, and a 200 response is sent. If the move is
        invalid, a 400 Bad Request response is sent
    '''
    if not check_valid_jwt('Authorization')[5::]:
        return make_response(401)

    move = request.get_json()['vote']

    if move in board.legal_moves():
        if votes[move]:
            votes[move] += 1
        else:
            votes[move] = 1

        return make_response(200)
    else:
        return make_response(400)

def make_move():
    '''Make a move based on current voter statistics, and check for endgame
       results. Designed to be run on threading.Timer
    '''
    move = None
    mostVotes = 0
    
    # Check votes{} for the highest voted ove
    for val in votes:
        if votes[val] > mostVotes:
            move = val
            mostVotes = votes[val]

    # If there are no votes, reset the timer and check again later
    if not move:
        return None

    board.push(chess.Move.uci(move))  # Push the most voted move to the board

    votes = {}                        # Reset votes for current move

    # Check if a team has just won the game
    result = board.result()
    
    # White has just won
    if result is '1-0':
        # Go through and update the player stats for all participants
        for player in votersCurrentMatch:
            playerData = users.find_one({'username': player})
            
            if playerData['team'] is 0:
                playerData['wins']++
            else:
                playerData['losses']++

            users.save(playerData)

        # Reset the board and voter stats, starting a new game
        board = chess.Board()
        voters = []
        votersCurrentMatch = []
    
    # Black has just won
    else if result is '1-0':
        for player in votersCurrentMatch:
            playerData = users.find_one({'username': player})
            
            if playerData['team'] is 0:
                playerData['losses']++
            else:
                playerData['wins']++

            users.save(playerData)

        board = chess.Board()
        voters = []
        votersCurrentMatch = []

    # There was a tie
    else if result is '1/2-1/2':
        board = chess.Board()
        voters = []
        votersCurrentMatch = []

    # There was no result this round
    else:
        None

@api.route('/player/<username>', methods=['GET'])
def send_player_stats(username):
    '''Send the statistics for a given username.
    Returns:
        If the player userame is not in the database, return a 404 Not Found
        response. If a player is found, return a 200 application/json response
        with the player statistics in the body
    '''
    # Search the users collection for a matching username
    player = users.find_one({'username': username})

    # If a player isnt found, return 404
    if not player:
        return make_response(404)

    player.pop('_id', None)       # Trim the MongoDB _id key off
    player.pop('password', None)  # Trim the password key off

    return make_response(jsonify(player), 200)
