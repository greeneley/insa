import matplotlib.pyplot as plt

D = {u'Label0':26, u'Label1': 17, u'Label2':30}

plt.bar(range(len(D)), D.values(), align='center')
plt.xticks(range(len(D)), D.keys())
plt.show()