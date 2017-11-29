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

# Initialize JWT utilities
secret = 'secret'  # Get the secret
jwt = JWT()        # Init JWT encoder/decoder

# Initialize MongoDB utilities
mongo = MongoClient('localhost', 27017)  # Connect to the database
db = mongo.democrachess                  # Get the democrachess database
users = db.users                         # Get the users collection

# Initialize chess helper variables
currentTeamMove = 0      # 0 == white team, 1 == black team
voters = []              # Array of who has voted for the current move
votersCurrentMatch = []  # Array of all voters over the course of the match

@api.route('/register', methods=['POST'])
def register():
    # Get the username and password from the request body
    username = request.get_json()['username']
    password = request.get_json()['password']

@api.route('/auth', methods=['POST'])
def auth():
    ''' Authenticate a user with the API, checks username and password
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
    # Send the game state, with a list of peices, their locations, and vote stats for each move if it is their team's turn

@api.route('/game', methods=['POST'])
def get_move():
    # Get a move vote, check if it's a logal move, and send a message back to the voter

def check_for_victory():
    # Check for a victory on the board. If there is, reset the board and alter player statistics for the participants

@api.route('/player/<id>', methods=['GET'])
def send_player_stats(id):
    # Send statisctics and information for a certain player
