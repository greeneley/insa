str_blc = 'abcdefghijklmnopq'

f = open('blc.tmp', 'w')
for i in range(16):
	f.write(str(i)+"->"+str(i+1)+" ["+str_blc[i]+"1];\n")
f.close()

