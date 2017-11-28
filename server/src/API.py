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

currentTeamMove = 0  # 0 == white team, 1 == black team
voters = []          # Array of who has voted for the current move

@api.route('/auth')
def auth():
    secret = 'secret'
    payload = {
                'iss': 'http://www.vxhvx.com/democrachess'
                'sub': 'username goes here'
              }
    jwt = JWT()
    jws = jwt.encode(payload, secret, algorithm='HS256')

@api.route('/game', methods=["GET"])
def send_game_state():
    # Send the game state, with a list of peices, their locations, and vote stats for each move if it is their team's turn

@api.route('/game', methods=["POST"])
def get_move():
    # Get a move vote, check if it's a logal move, and send a message back to the voter

def check_for_victory():
    # Check for a victory on the board. If there is, reset the board and alter player statistics for the participants

@api.route('/player/<id>', methods=["GET"])
def send_player_stats(id):
    # Send statisctics and information for a certain player
