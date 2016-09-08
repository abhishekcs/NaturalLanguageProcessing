
import glob
import random
import sys

def get_sentence_reader(lines):
	sentence = ''
	found_start = False
	for line in lines:
		line = unicode(line, 'utf-8')
		if(line[0:3] == '( (') or line[0:2] == '((':
			found_start = True
			if(sentence is not ''):
				yield sentence
			sentence = ''
		if found_start:
			sentence = sentence + line

training_sentences = []
test_senteces = []
sentences = []
list_of_genres = glob.glob('*/')
print list_of_genres
for genre in list_of_genres:
	sentences_per_genre = []
	list_of_files_in_genre = glob.glob(genre + '*.mrg')
	# print list_of_files_in_genre
	for individual_file in list_of_files_in_genre:
		with open(individual_file) as f:
			lines = f.readlines()
			sentence_reader = get_sentence_reader(lines)
			for sentence in sentence_reader:
				sentences_per_genre.append(sentence)
	total_number_of_sentences = len(sentences_per_genre)
	print total_number_of_sentences
	total_train_senteces = 9 * total_number_of_sentences/10
	training_sentences.extend(sentences_per_genre[:total_train_senteces])
	test_senteces.extend(sentences_per_genre[total_train_senteces:])


print len(training_sentences)
print len(test_senteces)
random.shuffle(training_sentences)


train_file_path = sys.argv[1]
test_file_path = sys.argv[2]

with open(train_file_path, 'w') as f:
	for sentence in training_sentences:
		f.write(sentence )

with open(test_file_path, 'w') as f:
	for sentence in test_senteces:
		f.write(sentence)

