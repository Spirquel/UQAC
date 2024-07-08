import csv

File_Path_ = 'pornhub.com-db/ph-db.csv'
output_file_path = 'pornhub.com-db/txt/texte.txt'

String_Search_ = '<iframe src="https://www.pornhub.com/embed/1914056039" frameborder="0" height="481" width="608" scrolling="no"></iframe>'

tab = []

with open(File_Path_, 'r', encoding='utf-8') as csvfile:
    csvreader = csv.reader(csvfile, delimiter=';')
    i = 1
    for row in csvreader:
          if i == 109031:
             #print("ligne {} : {}".format(i, row))
          # if row[7] == "invalid_date":
          #    print(i)
          #    tab.append(i)
               print(row)
          i += 1
        #     #tab.append("ligne {} : {}".format(i, row))
        # i += 1
        # if row[0] == String_Search_:
        #     print(row)

#with open(output_file_path, 'w', encoding='utf-8') as output_file:
#     for index in tab:
#         output_file.write(f'{index}\n')
        #[0] id_frame + src
        #[1] image + date
        #[2] images (à fuir)
        #[3] title
        #[4] tags
        #[5] categories
        #[6] pornstar
        #[7] times
        #[8] views
        #[9] likes
        #[10] dislikes
        #[11] image + date (simple)
        #[12] images (à fuir)