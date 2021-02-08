import pandas as pd
import os

dirPath = os.path.abspath(os.getcwd())

DF = pd.read_excel(dirPath + "\\Receiver\\resources\\2021.xlsx", engine='openpyxl')

def printDF():
    print(DF)