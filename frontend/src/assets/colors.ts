
// 1. La Palette (Vert en premier)
export const palette = [
  '#3cb44b', // Vert
  '#e6194b', // Rouge
  '#ffe119', // Jaune
  '#4363d8', // Bleu
  '#f58231', // Orange
  '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe'
]

// 2. Dictionnaire pour FORCER des couleurs logiques
export const forcedColors: Record<string, string> = {
  // Positif (Verts)
  'Oui': '#3cb44b', 'Oui, totalement': '#3cb44b', 'Oui (Pro)': '#3cb44b',
  'Oui (Perso)': '#808000', 'Pour': '#3cb44b', 'Une chance': '#3cb44b',
  'Confiance': '#3cb44b', 'Positif': '#3cb44b', 'Très tolérante': '#3cb44b',
  'Fort (actions concrètes)': '#3cb44b',

  // Négatif (Rouges)
  'Non': '#e6194b', 'Non, jamais': '#e6194b', 'Non, pas du tout': '#e6194b',
  'Contre': '#e6194b', 'Une contrainte': '#e6194b', 'Aucune confiance': '#e6194b',
  'Négatif': '#e6194b', 'Pas assez tolérante': '#e6194b',
  'Faible (ce n\'est pas ma priorité)': '#e6194b',

  // Neutre / Mitigé
  'Peut-être': '#ffe119', 'Mitigé': '#f58231',
  'Indifférent': '#cccccc', 'Je ne vote pas': '#cccccc',
  'Ça va': '#4363d8', 'Moyennement': '#f58231', 'C\'est compliqué': '#f58231'
}

// 3. La fonction exportée pour récupérer la couleur
export const getColorForText = (text: string) => {
  if (!text) return '#cccccc'; // Gris par défaut

  // A. Si le mot est dans notre dictionnaire forcé
  if (forcedColors[text]) {
    return forcedColors[text];
  }

  // B. Sinon, hash basé sur le texte
  let hash = 0;
  for (let i = 0; i < text.length; i++) {
    hash = text.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % palette.length;
  return palette[index];
}
