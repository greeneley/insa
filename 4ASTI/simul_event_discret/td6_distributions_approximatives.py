#!/usr/bin/python3.5

# ==========================================
#                  IMPORT
# ==========================================
import scipy.stats as stats


# ==========================================
#                  IMPORT
# ==========================================
NOMBRE_ECHANTILLONS = 1000


# ==========================================
# >>>>>>>>>>>>>> EXERCICE 1 <<<<<<<<<<<<<<<<
# ==========================================

print("===== Exponentielle(5) =====")
loi_exp = stats.expoen()
r_exp = stats.expoen.rvs(size=NOMBRE_ECHANTILLONS)
print("--- MLE")
print(stats.fit(r_exp))
print("--- Moments")
print("N'existe pas sous scipy ...")

print("===== Uniforme(10, 50) =====")
r_exp = stats.uniform.rvs(size=NOMBRE_ECHANTILLONS)
print("--- MLE")
print(stats.fit(r_exp))
print("--- Moments")
print("N'existe pas sous scipy ...")

print("===== Normale(15, 10)  =====")
r_exp = stats.norm.rvs(size=NOMBRE_ECHANTILLONS)
print("--- MLE")
print(stats.fit(r_exp))
print("--- Moments")
print("N'existe pas sous scipy ...")

