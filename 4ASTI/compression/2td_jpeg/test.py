a = [i for i in range(64)]

for x in range(8):
	print(a[x*8:(x*8)+8])

b = []
x=0
y=0
print((x,y))
for _ in range(3):
	b.append(a[x*8 + y])
	y+=1
	while(y!=0):
		print((x,y))
		b.append(a[x*8 + y])
		y-=1
		x+=1
	b.append(a[x*8 + y])
	x+=1
	while(x!=0):
		b.append(a[x*8 + y])
		x-=1
		y+=1
	b.append(a[x*8 + y])
	print("=============")

print(b)

