import pandas as pd
from datetime import datetime
import re

Input_File_Path_ = 'pornhub.com-db/pornhub.com-db-line.csv'
Output_File_Path_ = 'pornhub.com-db/ph-db.csv'

# Définir la taille des chunks
chunk_size = 500000

# Définir les colonnes du DataFrame final
Columns = ['title', 'src', 'tags', 'categories', 'pornstars', 'views', 'likes', 'dislikes', 'date', 'times']

# Fonction pour traiter chaque chunk
def process_chunk(chunk):
    processed_rows = []

    for index, row in chunk.iterrows():
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
            try:
                date_string = date.group(1)
                year_month_day = datetime.strptime(date_string, '%Y%m/%d')
                formatted_date = year_month_day.strftime('%d/%m/%Y')
                new_row['date'] = formatted_date
            except ValueError:
                new_row['date'] = ''
        else:
            new_row['date'] = ''
        
        if row.iloc[7] == '':
            new_row['times'] = 'no_times'
        else:
            new_row['times'] = row.iloc[7]

        processed_rows.append(new_row)

    return pd.DataFrame(processed_rows, columns=Columns)

# Lire et traiter le fichier par chunks
with pd.read_csv(Input_File_Path_, delimiter='|', encoding='utf-8', chunksize=chunk_size) as reader:
    for i, chunk in enumerate(reader):
        print(f"Processing chunk {i + 1}")
        processed_chunk = process_chunk(chunk)
        if i == 0:
            processed_chunk.to_csv(Output_File_Path_, index=False, sep=';', encoding='utf-8')
        else:
            processed_chunk.to_csv(Output_File_Path_, index=False, sep=';', encoding='utf-8', mode='a', header=False)

print("Traitement et écriture dans le nouveau fichier CSV terminés.")
