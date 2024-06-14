from flask import Flask, request
import mysql.connector

def create_connection():
    try:
        connection = mysql.connector.connect(
            host='127.0.0.1',
            user='root',
            password='',
            database='imc'
        )
        return connection
    except mysql.connector.Error as e:
        print("Erreur lors de la connexion à MySQL: {}".format(e))
        return None

imc = Flask(__name__)

@imc.route('/submit-imc', methods=['POST'])
def submit_imc():
    try:
        poids = request.form['Poids']
        taille = request.form['Taille']
        prenom = request.form['Prenom']
        nom = request.form['Nom']
        
        connection = create_connection()
        if connection is not None:
            cursor = connection.cursor()
            query = "INSERT INTO imu_user (prenom, nom, poids, taille) VALUES (%s, %s, %s, %s)"
            cursor.execute(query, (prenom, nom, poids, taille))
            connection.commit()
            cursor.close()
            connection.close()
            return 'IMC soumis avec succès'
        else:
            return "Erreur de connexion à la base de données"
    except Exception as e:
        return "Une erreur est survenue: {}".format(e)

if __name__ == '__main__':
    imc.run(debug=True)
