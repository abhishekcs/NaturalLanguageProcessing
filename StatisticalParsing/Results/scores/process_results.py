import glob

wsj_in_domain = glob.glob('part4*indomain.err')
wsj_brown_seedvar = glob.glob('part4*outofdomain.err')
wsj_brown_selftrvar = glob.glob('part5*')
brown_in_domain = glob.glob('part6_seedvar*indomain.err')
brown_wsj_seedvar = glob.glob('part6_seedvar*outofdomain.err')
brown_wsj_selftrvar = glob.glob('part6_selftrvar*')




def fetch_f1_values_from_single_file(file_name):
	fetch_current = False
	f1_values = []
	ind = 0
	with open(file_name) as f:
		lines = f.readlines()
		for line in lines:
			tokens = line.split()
			if(fetch_current):
				f1_values.append(float(tokens[ind]))
				fetch_current = False
			else:
				ind = 0
				for token in tokens:
					if token == 'factF1':
						fetch_current = True
						break
					ind += 1

	return f1_values


import re
import collections
def fetch_f1_values_from_list_of_files(list_of_files):
	dict_of_f1_values = {}
	for file_name in list_of_files:
		ints = map(int, re.findall(r'\d+', file_name))
		ints = filter(lambda x: x > 10, ints)
		size = ints[0]
		dict_of_f1_values[size] = fetch_f1_values_from_single_file(file_name)
	od = collections.OrderedDict(sorted(dict_of_f1_values.items()))
	return od


wsj_in_domain_vals = map(lambda x:x[0], fetch_f1_values_from_list_of_files(wsj_in_domain).values())
print 'WSJ INDOMAIN', wsj_in_domain_vals
vals = fetch_f1_values_from_list_of_files(wsj_brown_seedvar).values()
wsj_brown_noadapt = map(lambda x:x[0], vals)
wsj_brown_adapt = map(lambda x:x[1], vals)
print 'WSJ BROWN OUT OF DOMAIN NO ADAPTATION', wsj_brown_noadapt
print 'WSJ BROWN OUT OF DOMAIN ADAPTATION', wsj_brown_adapt
print '\n'
wsj_brown_selftrvar_vals = map(lambda x:x[1],fetch_f1_values_from_list_of_files(wsj_brown_selftrvar).values())
print 'WSJ BROWN OUT OF DOMAIN SELF TRAINING VAR', wsj_brown_selftrvar_vals
print '\n'
brown_in_domain_vals = map(lambda x:x[0],fetch_f1_values_from_list_of_files(brown_in_domain).values())
print 'BROWN INDOMAIN', brown_in_domain_vals
vals = fetch_f1_values_from_list_of_files(brown_wsj_seedvar).values()
brown_wsj_noadapt = map(lambda x:x[0], vals)
brown_wsj_adapt = map(lambda x:x[1], vals)
print 'BROWN WSJ OUT OF DOMAIN NO ADAPTATION', brown_wsj_noadapt
print 'BROWN WSJ OUT OF DOMAIN ADAPTATION', brown_wsj_adapt
print '\n'
brown_wsj_selftrvar_vals = map(lambda x:x[1],fetch_f1_values_from_list_of_files(brown_wsj_selftrvar).values())
print 'BROWN WSJ OUT OF DOMAIN SELF TRAINING VAR', brown_wsj_selftrvar_vals


import matplotlib.pyplot as plt
fig, ax = plt.subplots()
keys = [1000,2000,3000,4000,5000,7000,10000,13000,16000,20000,25000,30000,35000]
ax.set_xlabel('Seed Data Size')
ax.set_ylabel('F1 Score(%)')
ax.plot(keys, wsj_in_domain_vals,  label='WSJ IN DOMAIN')
ax.plot(keys, wsj_brown_noadapt,  label='WSJ TO BROWN WITHOUT ADAPTATION')
ax.plot(keys, wsj_brown_adapt, label='WSJ TO BROWN WITH ADAPTATION')
legend = ax.legend(loc = 'lower right', shadow=True)

# The frame is matplotlib.patches.Rectangle instance surrounding the legend.
frame = legend.get_frame()
frame.set_facecolor('0.90')
fig.savefig('wsj_brown_seed')




fig, ax = plt.subplots()
ax.set_xlabel('Self Train Data Size')
ax.set_ylabel('F1 Score(%)')
ax.plot(keys, brown_wsj_selftrvar_vals,  label='BROWN TO WSJ SELF-TRAIN VARIATION')
legend = ax.legend(loc = 'lower right', shadow=True)

# The frame is matplotlib.patches.Rectangle instance surrounding the legend.
frame = legend.get_frame()
frame.set_facecolor('0.90')
fig.savefig('brown_wsj_selftrvar')




fig, ax = plt.subplots()
keys = [1000,2000,3000,4000,5000,7000,10000,13000,17000,21000]
ax.set_xlabel('Seed Data Size')
ax.set_ylabel('F1 Score(%)')
ax.plot(keys, brown_in_domain_vals,  label='BROWN IN DOMAIN')
ax.plot(keys, brown_wsj_noadapt,  label='BROWN TO WSJ WITHOUT ADAPTATION')
ax.plot(keys, brown_wsj_adapt, label='BROWN TO WSJ WITH ADAPTATION')
legend = ax.legend(loc = 'lower right', shadow=True)

# The frame is matplotlib.patches.Rectangle instance surrounding the legend.
frame = legend.get_frame()
frame.set_facecolor('0.90')
fig.savefig('brown_wsj_seed')




fig, ax = plt.subplots()
ax.set_xlabel('Self Train Data Size')
ax.set_ylabel('F1 Score(%)')
ax.plot(keys, wsj_brown_selftrvar_vals,  label='WSJ TO BROWN SELF-TRAIN VARIATION')
legend = ax.legend(loc = 'lower right', shadow=True)

# The frame is matplotlib.patches.Rectangle instance surrounding the legend.
frame = legend.get_frame()
frame.set_facecolor('0.90')
fig.savefig('wsj_brown_selftrvar')




