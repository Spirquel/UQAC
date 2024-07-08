#modif txt file and replace argument

old_file = r'lines.txt'

with open(old_file, 'r', encoding='utf-8') as file:
    content = file.read()

old_text = r"line" #text to supp or modify
new_text = ''
content_modifie = content.replace(old_text, new_text)

new_file = r'line.txt' #new file

with open(new_file, 'w', encoding='utf-8') as file:
    file.write(content_modifie)

print(f"Le fichier a été modifié et enregistré sous '{new_file}'")