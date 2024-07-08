from flask import Flask, request, render_template, redirect, url_for, send_from_directory
import mysql.connector
import os

app = Flask(__name__, static_folder='../html_css/css', template_folder='../html_css/html')

# Sert les fichiers CSS à partir du répertoire spécifié
@app.route('/css/<path:filename>')
def serve_css(filename):
    return send_from_directory('../html_css/css', filename)

# Sert les fichiers d'images à partir du répertoire spécifié
@app.route('/img/<path:filename>')
def serve_image(filename):
    return send_from_directory('../html_css', filename)

# Crée et retourne une connexion à la base de données MySQL
def create_connection():
    connection = None
    try:
        connection = mysql.connector.connect(
            host='127.0.0.1',
            user='root',
            password='',
            database='imc'
        )
    except mysql.connector.Error as e:
        print(f"Erreur lors de la connexion à MySQL: {e}")
    return connection

# Détermine la classe d'IMC et la couleur correspondante en fonction de l'IMC
def determine_classe(imc):
    if imc < 18.5:
        return 'Maigreur', '#02abee'
    elif 18.5 <= imc < 25:
        return 'Normal', '#6fb12e'
    elif 25 <= imc < 30:
        return 'Surpoids', '#fd9b1a'
    elif 30 <= imc < 40:
        return 'Obésité modérée', '#f86e0a'
    elif imc >= 40:
        return 'Obésité sévère', '#ee010d'
    else:
        return 'Error', '#ff0000'

# Affiche la page d'accueil
@app.route('/')
def home():
    return render_template('acceuil.html')

# Affiche la page pour calculer l'IMC
@app.route('/calcul_imc.html')
def calcul_imc():
    return render_template('calcul_imc.html')

# Soumet et enregistre les données de l'IMC, puis redirige vers la page des résultats
@app.route('/submit-imc', methods=['POST'])
def submit_imc():
    poids = float(request.form['weight'])
    taille = float(request.form['height']) / 100
    prenom = request.form['first-name']
    nom = request.form['last-name']

    imc = poids / (taille ** 2)
    classe, couleur = determine_classe(imc)

    connection = create_connection()
    if connection:
        cursor = connection.cursor()
        query = "INSERT INTO imu_user (prenom, nom, imc, classe) VALUES (%s, %s, %s, %s)"
        cursor.execute(query, (prenom, nom, imc, classe))
        connection.commit()
        cursor.close()
        connection.close()

        return redirect(url_for('result_imc', imc=imc, classe=classe, couleur=couleur))
    return "Erreur de connexion à la base de données"

# Affiche les résultats de l'IMC pour l'utilisateur
@app.route('/result_imc.html')
def result_imc():
    imc = request.args.get('imc', type=float)
    classe = request.args.get('classe')
    couleur = request.args.get('couleur')
    return render_template('result_imc.html', imc=imc, classe=classe, couleur=couleur)

# Affiche toutes les données d'IMC enregistrées dans la base de données
@app.route('/all_data.html')
def all_data():
    connection = create_connection()
    if connection:
        cursor = connection.cursor(dictionary=True)
        cursor.execute("SELECT prenom, nom, imc, classe FROM imu_user")
        data = cursor.fetchall()
        cursor.close()
        connection.close()
        return render_template('all_data.html', data=data)
    return "Erreur de connexion à la base de données"

# Démarre l'application Flask
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
