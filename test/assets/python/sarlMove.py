from unit import Unit
from unitView import UnitView
from pos import Pos
from logger import debug_print

import math
import numpy as np
import random
import matplotlib.pyplot as plt
import numpy as np


actions = 9
#maximum of iteration per episode
max_iter_episode = 30
#initialize the exploration probability to 1
exploration_proba = 1
#exploartion decreasing decay for exponential decreasing
exploration_decreasing_decay = 0.001
# minimum of exploration proba
min_exploration_proba = 0.01
#discounted factor
gamma = 0.99
#learning rate
lr = 0.01
#TODO: a qtables az tipus alapjan azonositja a unitokat
#Q_table
Q_table = np.zeros((16*16, actions))


file = open("rewards.txt", "w")
totalCounteer = 1
runcounter = 0
total_episode_reward = 0
total_couter_all = list()
total_rewards_per_episode = list()
movement_map = np.zeros((16, 16))




# sampl = np.random.uniform(low=-5, high=0, size=(10,10))
# plt.imshow(sampl, cmap='hot', interpolation='lanczos')
# plt.show()


def sarlMove(unit):
	global exploration_proba
	global runcounter
	global total_episode_reward
	global totalCounteer
	debug_print(exploration_proba, runcounter)
	#we iterate over episodes

	current_state_idx = toidx(unit.field.pos)
	# we sample a float from a uniform distribution over 0 and 1
	# if the sampled flaot is less than the exploration proba
	#	 the agent selects arandom action
	# else
	#	 he exploits his knowledge using the bellman equation 
	
	if np.random.uniform(0,1) < exploration_proba:
		action = random.randint(0, 8)
	else:
		action = np.argmax(Q_table[current_state_idx,:])
	
	# The environment runs the chosen action and returns
	# the next state, a reward and true if the epiosed is ended.
	next_state = nextState(unit.field.pos, action)
	reward = rewardCalc(unit, next_state)
	debug_print(f"current reward: {reward}")
	
	# We update our Q-table using the Q-learning iteration
	Q_table[current_state_idx, action] = (1-lr) * Q_table[current_state_idx, action] + lr*(reward + gamma*max(Q_table[toidx(next_state),:] - Q_table[current_state_idx, action]))

	total_episode_reward = total_episode_reward + reward
	# If the episode is finished, we leave the for loop

	#We update the exploration proba using exponential decay formula 
	exploration_proba = max(min_exploration_proba, np.exp(-exploration_decreasing_decay*totalCounteer))
	
	# global rewards_per_episode
	# rewards_per_episode.append(total_episode_reward)

	totalCounteer += 1
	total_couter_all.append(totalCounteer)
	total_rewards_per_episode.append(total_episode_reward)
	runcounter += 1

	if(totalCounteer > 100):
		plt.rcParams["figure.autolayout"] = True
		plt.subplot(1, 3, 1)
		plt.title("Q_table values heatmap")
		plt.imshow(Q_table, cmap='hot', interpolation='nearest')
		plt.subplot(1, 3, 2)
		plt.title("total rewards / episode")
		plt.plot(total_couter_all, total_rewards_per_episode)
		plt.subplot(1, 3, 3)
		plt.title("movement heatmap")
		plt.imshow(np.flipud(movement_map), cmap='hot', interpolation='nearest')
		plt.show()

	if(runcounter == max_iter_episode):
		# file.write(f"{total_episode_reward} \n")
		# file.flush()
		runcounter = 0
		total_episode_reward = 0
		return "reset"
	movement_map[unit.field.pos.x, unit.field.pos.y] += 1
	return f"move {unit.id} {next_state.x} {next_state.y}"

def rewardCalc(unit, next_state):
	if(len(unit.seenControlPoints) == 0):
		return -10
	currDist = unit.field.pos.dist(unit.seenControlPoints[0].pos)
	if(unit.field.pos.euclDist(unit.seenControlPoints[0].pos) < unit.seenControlPoints[0].size):
		return 200
	if(unit.field.pos == next_state):
		return -20
	return (16 - currDist)


def toidx(pos):
	return pos.x * 16 + pos.y

def fromidx(idx):
	return Pos([idx // 16, idx % 16])

def nextState(state, action):
	next_state = None
	match action:
		case 0:
			next_state = Pos([state.x + 0, state.y + 0])
		case 1:
			next_state =  Pos([state.x + 1, state.y + 0])
		case 2:
			next_state = Pos([state.x + 1, state.y + 1])
		case 3:
			next_state = Pos([state.x + 0, state.y + 1])
		case 4:
			next_state = Pos([state.x - 1, state.y + 1])
		case 5:
			next_state = Pos([state.x - 1, state.y + 0])
		case 6:
			next_state = Pos([state.x - 1, state.y - 1])
		case 7:
			next_state = Pos([state.x + 0, state.y -1])
		case 8:
			next_state = Pos([state.x + 1, state.y - 1])
	if(next_state is not None):
		next_state.x = min(15, max(0, next_state.x))
		next_state.y = min(15, max(0, next_state.y))
		return next_state