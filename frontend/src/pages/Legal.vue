<script setup lang="ts">
import { ref } from 'vue'

const activeTab = ref('mentions') // 'mentions' ou 'privacy'

// Fonction pour changer d'onglet
const setTab = (tab: string) => {
  activeTab.value = tab
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const currentDate = new Date().toLocaleDateString('fr-FR')
</script>

<template>
  <div class="page-container">

    <header class="header">
      <h1>Informations <span class="highlight">Légales</span></h1>
      <p class="subtitle">Transparence et protection de vos données</p>

      <div class="tabs">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'mentions' }"
          @click="setTab('mentions')"
        >
          ⚖️ Mentions Légales
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'privacy' }"
          @click="setTab('privacy')"
        >
          🔒 Politique de Confidentialité
        </button>
      </div>
    </header>

    <div class="content-wrapper fade-in">

      <div v-if="activeTab === 'mentions'" class="legal-content">
        <section class="section-block">
          <h2>1. Éditeur du Site</h2>
          <p>
            Le site internet <strong>Goat Trotters</strong> est édité par l'association <em>Goat Trotters</em> (Association régie par la loi du 1er juillet 1901).
          </p>
          <ul class="details-list">
            <li><strong>Siège Social :</strong> 40 La Poupetiere Saint-Aubin, 79700 Mauléon</li>
            <li><strong>Email de contact :</strong> <a href="mailto:goat.trotters@gmail.com">goat.trotters@gmail.com</a></li>
            <li><strong>Directeur de la publication :</strong> Dorian ROBINEAU</li>
          </ul>
        </section>

        <section class="section-block">
          <h2>2. Hébergement</h2>
          <p>
            Le site est hébergé par la société <strong>Microsoft Azure</strong>
          </p>
          <ul class="details-list">
            <li><strong>Adresse :</strong> [Adresse de l'hébergeur]</li>
            <li><strong>Site web :</strong> [Lien vers l'hébergeur]</li>
          </ul>
        </section>

        <section class="section-block">
          <h2>3. Propriété Intellectuelle</h2>
          <p>
            L’ensemble de ce site relève de la législation française et internationale sur le droit d’auteur et la propriété intellectuelle.
            Tous les droits de reproduction sont réservés, y compris pour les documents téléchargeables et les représentations iconographiques et photographiques.
          </p>
          <p>
            Toute reproduction, modification, publication, adaptation de tout ou partie des éléments du site, quel que soit le moyen ou le procédé utilisé,
            est interdite, sauf autorisation écrite préalable de l'association Goat Trotters.
          </p>
        </section>
      </div>

      <div v-if="activeTab === 'privacy'" class="legal-content">
        <div class="info-box">
          <p>Dernière mise à jour : {{ currentDate }}</p>
        </div>

        <section class="section-block">
          <h2>1. Collecte des Données</h2>
          <p>
            Dans le cadre de notre projet de visualisation de données et de sondages, nous collectons les informations suivantes via nos formulaires :
          </p>
          <ul class="check-list">
            <li>Pays de résidence</li>
            <li>Tranche d'âge</li>
            <li>Genre</li>
            <li>Catégorie socio-professionnelle</li>
            <li>Opinions exprimées lors des sondages</li>
          </ul>
          <p class="note">
            Ces données sont collectées <strong>uniquement</strong> à des fins statistiques et pédagogiques.
          </p>
        </section>

        <section class="section-block">
          <h2>2. Utilisation des Données</h2>
          <p>Les données recueillies nous permettent de :</p>
          <ul>
            <li>Générer des statistiques globales et anonymes (Heatmap, Graphiques).</li>
            <li>Analyser les tendances sociétales liées au tourisme et à l'écologie.</li>
            <li>Améliorer l'expérience utilisateur sur notre site.</li>
          </ul>
          <p>
            <strong>Aucune donnée n'est vendue ou cédée à des tiers.</strong>
          </p>
        </section>

        <section class="section-block">
          <h2>3. Vos Droits (RGPD)</h2>
          <p>
            Conformément au Règlement Général sur la Protection des Données (RGPD), vous disposez des droits suivants concernant vos données personnelles :
          </p>
          <div class="rights-grid">
            <div class="right-card">
              <h3>Droit d'accès</h3>
              <p>Savoir quelles données nous possédons sur vous.</p>
            </div>
            <div class="right-card">
              <h3>Droit à l'effacement</h3>
              <p>Demander la suppression de toutes vos réponses.</p>
            </div>
            <div class="right-card">
              <h3>Droit de rectification</h3>
              <p>Corriger une information erronée.</p>
            </div>
          </div>
          <p style="margin-top: 1.5rem;">
            Pour exercer ces droits, contactez-nous simplement à :
            <a href="mailto:goat.trotters@gmail.com" class="highlight-link">goat.trotters@gmail.com</a>
          </p>
        </section>

        <section class="section-block">
          <h2>4. Cookies</h2>
          <p>
            Ce site utilise uniquement des cookies techniques nécessaires au bon fonctionnement de la navigation (ex: sauvegarde de votre session de vote).
            Aucun cookie publicitaire ou de traçage commercial n'est utilisé.
          </p>
        </section>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* --- CONFIGURATION GLOBALE --- */
.page-container {
  min-height: 100vh;
  background-color: #1b1336;
  color: #e0e0e0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 4rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* --- HEADER & TABS --- */
.header {
  text-align: center;
  margin-bottom: 3rem;
  max-width: 800px;
}

.header h1 {
  font-size: 2.5rem;
  font-weight: 300;
  color: white;
  margin-bottom: 0.5rem;
}

.highlight {
  color: #D4AF37;
  font-weight: 700;
}

.subtitle {
  color: #aaa;
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.tabs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.tab-btn {
  background: transparent;
  border: 1px solid #444;
  color: #aaa;
  padding: 10px 20px;
  border-radius: 50px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.tab-btn:hover {
  border-color: #D4AF37;
  color: white;
}

.tab-btn.active {
  background: #D4AF37;
  color: #1b1336;
  border-color: #D4AF37;
  font-weight: bold;
  box-shadow: 0 0 15px rgba(212, 175, 55, 0.3);
}

/* --- CONTENU --- */
.content-wrapper {
  max-width: 900px;
  width: 100%;
  background: #2a2245;
  padding: 3rem;
  border-radius: 16px;
  border: 1px solid #444;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.section-block {
  margin-bottom: 2.5rem;
  border-bottom: 1px solid #333;
  padding-bottom: 2rem;
}

.section-block:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

h2 {
  color: #D4AF37;
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

p, li {
  line-height: 1.7;
  color: #ccc;
}

a {
  color: #D4AF37;
  text-decoration: none;
  transition: color 0.2s;
}
a:hover { color: white; }

/* Listes */
.details-list {
  list-style: none;
  padding: 0;
  margin-top: 1rem;
}
.details-list li {
  margin-bottom: 0.5rem;
  padding-left: 1rem;
  border-left: 2px solid #D4AF37;
}

.check-list {
  list-style: none;
  padding: 0;
}
.check-list li::before {
  content: '✓';
  color: #D4AF37;
  margin-right: 10px;
  font-weight: bold;
}

.note {
  background: rgba(212, 175, 55, 0.1);
  padding: 1rem;
  border-radius: 8px;
  margin-top: 1rem;
  border-left: 4px solid #D4AF37;
  font-size: 0.9rem;
}

.info-box {
  text-align: right;
  font-style: italic;
  font-size: 0.85rem;
  color: #888;
  margin-bottom: 1rem;
}

/* --- GRILLE DES DROITS --- */
.rights-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-top: 1.5rem;
}

.right-card {
  background: #1b1336;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #333;
  text-align: center;
}

.right-card h3 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.right-card p {
  font-size: 0.9rem;
  color: #888;
}

/* --- ANIMATION --- */
.fade-in {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 768px) {
  .content-wrapper { padding: 1.5rem; }
  .header h1 { font-size: 2rem; }
  .tabs { gap: 0.5rem; }
  .tab-btn { font-size: 0.9rem; padding: 8px 15px; }
}
</style>
