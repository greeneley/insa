class Test(object):
	def __init__(self):
		self.n = "lol"
		self.p = "hehe"
		self.id = 5
		return

	def __str__(self):
		string = (
		"Personne id        : {}\n"
		"Nom                : {}\n"
		"Prenom             : {}\n"
		).format(5, "JL", "LOL")
		
		return string

a = Test()
print(type(a))

print([5,6,7,8,2,6,8,9].lenght)
