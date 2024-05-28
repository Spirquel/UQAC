import pandas as pd

# import csv

# with open("data/match_15m.csv") as data_csv:
#     reader = csv.reader(data_csv, delimiter=",", quotechar='"')
#     data_read = [row for row in reader]

# print(data_read)

DataFrame = pd.read_csv("data/match_15m.csv", sep=',')
print(DataFrame)
