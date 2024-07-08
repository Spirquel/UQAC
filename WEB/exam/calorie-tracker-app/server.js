const express = require('express');
const app = express();

const PORT = process.env.PORT || 3003;

app.use(express.static('public'));
app.use(express.json());

app.post('/calculate', (req, res) => {
    const { aliments } = req.body;
    let totalGlucides = 0;

    aliments.forEach(aliment => {
        totalGlucides += aliment.glucides;
    });

    let recommendation = '';
    if (totalGlucides < 15) {
        recommendation = 'extrêmement faible';
    } else if (totalGlucides >= 15 && totalGlucides < 30) {
        recommendation = 'faible';
    } else if (totalGlucides >= 30 && totalGlucides < 60) {
        recommendation = 'normal';
    } else {
        recommendation = 'très élevé';
    }

    res.json({ total: totalGlucides, recommendation });
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
