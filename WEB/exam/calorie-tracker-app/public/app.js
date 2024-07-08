document.addEventListener('DOMContentLoaded', () => {
    const alimentInput = document.getElementById('nom-aliment');
    const glucidesInput = document.getElementById('grammes-glucides');
    const ajouterButton = document.getElementById('ajouter-aliment');
    const tableauAliments = document.getElementById('tableau-aliments');
    const calculerButton = document.getElementById('calculer-glucides');
    const resultatContainer = document.getElementById('result-container');
    const resultatText = document.getElementById('resultat');

    let aliments = [];

    ajouterButton.addEventListener('click', () => {
        const nom = alimentInput.value.trim();
        const glucides = parseFloat(glucidesInput.value.trim());

        if (nom && !isNaN(glucides)) {
            aliments.push({ nom, glucides });
            afficherAliments();
            alimentInput.value = '';
            glucidesInput.value = '';
        }
    });

    tableauAliments.addEventListener('click', (event) => {
        if (event.target.classList.contains('supprimer-aliment')) {
            const index = event.target.dataset.index;
            aliments.splice(index, 1);
            afficherAliments();
        }
    });

    calculerButton.addEventListener('click', () => {
        fetch('/calculate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ aliments })
        })
        .then(response => response.json())
        .then(data => {
            resultatContainer.style.display = 'block';
            resultatText.textContent = `Total des glucides : ${data.total} g. Recommandation : ${data.recommendation}`;
        });
    });

    function afficherAliments() {
        tableauAliments.innerHTML = aliments.map((aliment, index) => `
            <tr>
                <td>${aliment.nom}</td>
                <td>${aliment.glucides} g</td>
                <td><button class="btn btn-link supprimer-aliment" data-index="${index}">Supprimer</button></td>
            </tr>
        `).join('');
    }
});
