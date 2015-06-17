from random import randint

dimension = 8000
tuples = 50
cardinality = 4

testID = 1
name = 'D' + str(dimension) + 'T' + str(tuples) + 'C' + str(cardinality) + '-' + str(testID)
output = open(name, 'w')

for t in range(tuples):
	line = ''
	for d in range(dimension):
		num = randint(0,cardinality)
		line += str(num) + ';'
	output.write(line[:-1] + '\n')

output.close()

