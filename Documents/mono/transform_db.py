# -*- coding: utf-8 -*-
#!/usr/bin/env python

# Converte os valores de uma tabela para seus correspondentes unicos (usado em data mining)

# f = open('/home/lauro/Desktop/data_set_ALL_AML_train', 'w')
f = open('/media/lauro/Data/Academic/computacao/15.1/monografiaII/backups/data_set_ALL_AML_independent_edited03.csv', 'r')
m = 0
tab = list()
# lines = f.readlines()[1:]
lines = f.readlines()
rowMax = len(lines)
for i in range(rowMax):
	# s = lines[i].split(',')
	s = lines[i].split(';')
	m = max(m, len(s) )
	tab.append( s )

uIndex = 0
myMap = dict()
newTable = dict()
for i in range(rowMax):
	newTable[i] = dict()

for i in range(m):
	myMap[i] = dict()
	for j in range(rowMax):
		tok = tab[j][i].strip()
		if tok != '':
			if tok not in myMap[i]:
				uIndex += 1
				myMap[i][tok] = uIndex
				val = uIndex
			else:
				val = myMap[i][tok]
			newTable[j][i] = val
f.close()




# f = open('/home/lauro/Desktop/data_set_ALL_AML_train_edited', 'w')
f = open('/media/lauro/Data/Academic/computacao/15.1/monografiaII/backups/data_set_ALL_AML_independent_edited04.csv', 'w')
for row in range(rowMax):
	line = ';'.join( [ str(newTable[row][col]) for col in range(m) if col in newTable[row].keys() ] )
	f.write(line+'\n')
f.close()