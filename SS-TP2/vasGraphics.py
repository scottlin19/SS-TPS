import matplotlib.pyplot as plt
import json

if __name__=="__main__":
    
    f = open('./results/dummyJson.json')

    jsonVAsData = json.load(f)
    print(jsonVAsData)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    dataFor1stGraph = jsonVAsData['data'][0]
    n = dataFor1stGraph["n"]
    etas = dataFor1stGraph["etas"]
    avgs = etas[0]["avgs"]
    iterations = len(avgs)

    titleText = "VAs en función del tiempo \nN = " + str(n) + " ETA = " + str(etas[0]["eta"])
    plt.title(titleText)
    plt.xlabel("Iteración")
    plt.ylabel("Va Promedio")

    stds = etas[0]["stds"]
    plt.errorbar(range(iterations), avgs, yerr=stds, fmt="o")
    plt.show()

    # -------
    