#!/usr/bin/python3.4

### Classe ###

class MaClasse(object):
	"""BREVE

		VAR

	LONGUE
	"""

	def __init__(self):
		"""Constructeur de l'objet MaClasse"""

	### SETTERS ###
	### GETTERS ###
	### PROPERTIES ###
	### METHODES ###

	pass # FIN DE CLASSE

### Fonction ###

def maFonction():
	"""BREVE

		VAR

	LONGUE
	"""

	pass # FIN DE FONCTION

# PyQt4 #
""" MEMO PRINCIPAUX POUR LA FENETRE
ACTIONS  : QtGui.QAction pour les actions des menusbar avec raccourcis, icon... '&' pour mettre un raccourcis rapide sur les noms
           .setStatusTips() pour faire afficher une aide dans la statusbar
           un signal 'triggered' est declenche sur clique de l'item

ICON     : dans l'init du main QWidget.setWindowIcon(QtGui.QIcon("monicone.ext"))   

TOOLTIP  : Infobulle via QtGui.setToolTip

TOOLBAR  : Barre d'outils via .addToolbar qui les ajoute de maniere sucessive

OVERRIDE : On peut modifier le comportement pas defaut en surchargeant les fonctions levees par defaut
           ex : closeEvent (lorsque l'on clique sur la croix de fermeture) d'un widget (fenetre par exemple)

STATUSBAR : via .statusbar()
            le premier appel creer le statusbar. Les prochains renvoient l'objet deja cree.

LAYOUTBOX : QBoxLayout 
            Utiliser .setLayout() pour ajouter une box dans une autre (ou dans un contenair)
            .addStretch(1) pour remplir de blanc de gauche à droite ou de haut en bas suivant la box.

GRIDLAYOUT : QtGui.QGridLayout
            .addWidget() pour ajouer un element dans la grille

AUTRES : On peut centrer un widget (qui prend alors toute la place restante) .setCentralWidget()
         
DRAG N DROP : QtGui.QDrag
         Pour le DRAG & DROP, il faut surcharger dragEnterEvent(self, e) dans la classe main
         Par consequent, il faut aussi surcharger dropEvent(self, e)
         Ne pas oublier de permettre le drag .setDragEnabled(True)
         Il est conseille de creer des classes personnalisees si l'on veut drag & drop

=== WIDGETS ===
BOUTON   : QtGui.QPushButton()
           Possibilite de faire des boutons pressoirs via .setCheckable(True)
            Dans ce cas, les events se font souvent avec [bool]
           .sizeHint() permet de donner une taille definie correcte
           .clicked.connect(nom_fonction) pour relier un signal a une action
           .setSpacing() pour mettre du spacing

LABEL    : QtGui.QLabel

CHECKBOX : QtGui.QCheckBox

IMAGE : QtGui.QPixmap

SPLITTER : QtGui.QSplitter

COMBOBOX : QtGui.QComboBox

BOITE DE DIALOGUE : Qt.QMessageBox

DIALOGUE FICHIER : QtGui.QFileDialog

LIGNE DE TEXTE : QtGui.QLineEdit()
ZONE DE TEXTE : QtGui.QTextEdit()
"""

# ====== Widgets ====== #

# ====== Signaux ====== #

# ===== Packaging ===== #
class Communicate(QtCore.QObject):
    
    closeApp = QtCore.pyqtSignal() 
    #On peut ensuite connecter ce sinal a un slot d'une auter classe
    #C'est donc ici un signal personnalise

class Example(QtGui.QMainWindow):
    
    def __init__(self):
        super(Example, self).__init__()
        
        self.initUI()

        
    def initUI(self):
        
        self.resize(250, 150)
        self.move(QtGui.QApplication.desktop().screen().rect().center()- self.frameGeometry().center())
        self.setWindowTitle('Icon')
        self.setWindowIcon(QtGui.QIcon('web.png'))        
    
        self.show()
        
        
def main():
    
    app = QtGui.QApplication(sys.argv)
    ex = Example()

    #Lance la boucle visuelle.
    #.exit() sert ici a definir le comportement "clean" pour quitter le programme
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()   