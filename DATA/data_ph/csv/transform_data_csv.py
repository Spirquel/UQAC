import pandas as pd
from datetime import datetime
import re

Input_File_Path_ = 'pornhub.com-db/pornhub.com-db-line.csv'
Output_File_Path_ = 'pornhub.com-db/ph-db.csv'

DataFrames_ = pd.read_csv(Input_File_Path_, delimiter='|', encoding='utf-8', nrows=1000)

Columns = ['title', 'src', 'tags', 'categories', 'pornstars', 'views', 'likes', 'dislikes', 'date', 'times']
New_DataFrame_ = pd.DataFrame(columns=Columns)
i = 1
j = 1
for index, row in DataFrames_.iterrows():
    if i == j:
        print("line {} :".format(i))
        j += 100
    
    new_row = {}

    if row.iloc[3] == '':
        new_row['title'] = 'no_title'
    else:
        new_row['title'] = row.iloc[3]

    src = re.search(r'src="([^"]+)"', row.iloc[0])
    new_row['src'] = src.group(1) if src else 'None'

    if row.iloc[4] == '':
        new_row['tags'] = 'no_tags'
    else:
        # if row.iloc[4].isin('ideepthroat.com'):
        #     new_row['tags'] = row.iloc[4].replace('ideepthroat.com;', '').strip()
        # else:
        new_row['tags'] = row.iloc[4]
 
    if row.iloc[5] == '':
        new_row['categories'] = 'no_categories'
    else:
        new_row['categories'] = row.iloc[5]
    print

    if row.iloc[6] == '':
        new_row['pornstars'] = 'no_name'
    else:
        new_row['pornstars'] = row.iloc[6]
    
    if row.iloc[8] == '':
        new_row['views'] = '0'
    else:
        new_row['views'] = row.iloc[8]

    if row.iloc[9] == '':
        new_row['likes'] = '0'
    else:
        new_row['likes'] = row.iloc[9]

    if row.iloc[10] == '':
        new_row['dislikes'] = '0'
    else:
        new_row['dislikes'] = row.iloc[10]

    date = re.search(r'/(\d{6}/\d{2})/', row.iloc[11])
    if date:
        date_string = date.group(1)
        year_month_day = datetime.strptime(date_string, '%Y%m/%d')
        formatted_date = year_month_day.strftime('%d-%m-%Y')
        new_row['date'] = formatted_date
    else:
        new_row['date'] = 'no_date'
    
    if row.iloc[7] == '':
        new_row['times'] = 'no_times'
    else:
        new_row['times'] = row.iloc[7]
    
    i += 1
    New_DataFrame_ = pd.concat([New_DataFrame_, pd.DataFrame([new_row])], ignore_index=True)

New_DataFrame_.to_csv(Output_File_Path_, index=False, sep=';', encoding='utf-8')

print("Traitement et écriture dans le nouveau fichier CSV terminés.")