<script setup lang="ts">
import { ref, onMounted, nextTick, watch, computed } from 'vue'
import axios from 'axios'
import * as L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import StatsChart from "@/components/StatsChart.vue";

// --- STATE ---
const stats = ref<any[]>([])
const loading = ref(true)
const error = ref('')
const questions = ref<any[]>([])
const selectedQuestion = ref<number | null>(null)

// Gestion de l'affichage des filtres
const showFilters = ref(false) // 👈 NOUVEAU : Par défaut masqué

const filters = ref({
  gender: '' as string,
  category: '' as string,
  ageRange: '' as string
})

const hasActiveFilters = computed(() => {
  return filters.value.gender !== '' ||
    filters.value.category !== '' ||
    filters.value.ageRange !== ''
})

const resetFilters = () => {
  filters.value.gender = ''
  filters.value.category = ''
  filters.value.ageRange = ''
  // Le watch déclenchera automatiquement le rechargement des stats
}

let map: L.Map | null = null
let geoLayer: L.GeoJSON<any> | null = null

// --- LOGIQUE (Reste identique) ---
const loadQuestions = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/questions')
    questions.value = res.data
  } catch (err) {
    console.error(err)
    error.value = "Impossible de charger les questions."
  }
}

const loadStats = async () => {
  if (!selectedQuestion.value) return
  loading.value = true
  error.value = ''

  try {
    let minAge = null
    let maxAge = null
    if (filters.value.ageRange) {
      const [min, max] = filters.value.ageRange.split('-')
      minAge = min
      maxAge = max
    }

    const response = await axios.get(`http://localhost:8080/api/responses/stats/${selectedQuestion.value}`, {
      params: {
        gender: filters.value.gender || null,
        category: filters.value.category || null,
        minAge: minAge,
        maxAge: maxAge
      }
    })

    stats.value = response.data
    updateMapColors()
  } catch (err) {
    console.error(err)
    error.value = 'Erreur lors du chargement des statistiques.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await loadQuestions()
  loading.value = false
  await nextTick()
  initMap()
})

// Réinitialiser l'affichage des filtres quand on change de question ?
// Ici je choisis de laisser ouvert si c'était déjà ouvert (plus agréable)
watch([selectedQuestion, filters], async ([newQ], [oldQ]) => {
  if (newQ) {
    await loadStats()
  }
}, { deep: true })

// --- MAP LOGIC ---
function initMap() {
  const mapContainer = document.getElementById('map')
  if (!mapContainer) return

  map = L.map(mapContainer, { center: [54, 15], zoom: 4, minZoom: 3 })

  L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; CARTO', subdomains: 'abcd', maxZoom: 19
  }).addTo(map)

  geoLayer = L.geoJSON(null, {
    style: { fillColor: '#333', weight: 1, opacity: 1, color: '#555', fillOpacity: 0.3 },
    onEachFeature: (feature, layer) => {
      if (feature.properties?.NAME) layer.bindTooltip(feature.properties.NAME)
    }
  }).addTo(map)

  fetch('https://raw.githubusercontent.com/leakyMirror/map-of-europe/master/GeoJSON/europe.geojson')
    .then(res => res.json()).then(data => { if (geoLayer) geoLayer.addData(data) })
}

function updateMapColors() {
  if (!geoLayer) return;
  geoLayer.eachLayer((layer: any) => { layer.setStyle({ fillColor: '#333', fillOpacity: 0.2 }) });

  if(!stats.value.length) return;

  geoLayer.eachLayer((layer: any) => {
    const props = layer.feature.properties;
    const geoName = props.NAME || props.name || props.ADMIN;
    const stat = stats.value.find((s: any) => s.country.toLowerCase() === geoName.toLowerCase());

    if (stat) {
      const opacity = 0.2 + (stat.yes / 100) * 0.8;
      layer.setStyle({ fillColor: '#D4AF37', fillOpacity: opacity, weight: 1, color: 'white' });
      layer.bindTooltip(`
        <div style="text-align: center;"><strong>${geoName}</strong><br/>
        <span style="color: #D4AF37;">Oui: ${stat.yes}%</span><br/>
        <span style="color: #ccc;">Non: ${stat.no}%</span></div>
      `);
    }
  });
}
</script>

<template>
  <div class="stats-container">

    <header class="stats-header">
      <h1>Observatoire des <span class="highlight">Données</span></h1>
      <p class="subtitle">Analysez les tendances et opinions à travers l'Europe.</p>
    </header>

    <div class="control-panel">

      <div class="control-row main-select">
        <label>Sujet de l'analyse :</label>
        <div class="select-wrapper">
          <select v-model="selectedQuestion">
            <option :value="null" disabled>-- Choisir une question --</option>
            <option v-for="q in questions" :key="q.id" :value="q.id">{{ q.text }}</option>
          </select>
          <div class="arrow-icon">▼</div>
        </div>
      </div>

      <div v-if="selectedQuestion" class="toggle-row fade-in">
        <button
          @click="showFilters = !showFilters"
          class="btn-toggle-filters"
          :class="{ 'active': showFilters }"
        >
          <svg v-if="!showFilters" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"></polygon>
          </svg>

          <svg v-else xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>

          <span>{{ showFilters ? 'Masquer les filtres' : 'Filtrer les résultats' }}</span>
        </button>

        <button
          v-if="hasActiveFilters"
          @click="resetFilters"
          class="btn-reset"
          title="Réinitialiser tous les filtres"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
          Effacer
        </button>
      </div>

      <Transition name="slide-fade">
        <div v-if="selectedQuestion && showFilters" class="filters-container">
          <div class="filters-grid">

            <div class="filter-group">
              <label>Genre</label>
              <select v-model="filters.gender">
                <option value="">Tous</option>
                <option value="Homme">Hommes</option>
                <option value="Femme">Femmes</option>
                <option value="Autre">Autres</option>
              </select>
            </div>

            <div class="filter-group">
              <label>Âge</label>
              <select v-model="filters.ageRange">
                <option value="">Tous les âges</option>
                <option value="15-25">15 - 25 ans</option>
                <option value="26-40">26 - 40 ans</option>
                <option value="41-60">41 - 60 ans</option>
                <option value="61-99">61 ans et +</option>
              </select>
            </div>

            <div class="filter-group">
              <label>Statut</label>
              <select v-model="filters.category">
                <option value="">Tous statuts</option>
                <option>Étudiant(e)</option>
                <option>Employé(e)</option>
                <option>Cadre</option>
                <option>Ouvrier(ère)</option>
                <option>Retraité(e)</option>
                <option>Sans emploi</option>
              </select>
            </div>

          </div>
        </div>
      </Transition>

    </div>

    <div v-if="error" class="error-msg">{{ error }}</div>

    <div class="map-wrapper">
      <div v-if="loading" class="loading-overlay"><div class="spinner"></div></div>
      <div id="map" class="map"></div>
      <div class="map-legend" v-if="selectedQuestion">
        <span>0% Oui</span><div class="gradient-bar"></div><span>100% Oui</span>
      </div>
    </div>

    <div v-if="stats.length > 0" class="charts-section fade-in">
      <h2>Comparatif détaillé</h2>
      <div class="chart-container">
        <StatsChart :stats="stats" />
      </div>
    </div>

    <div v-else-if="selectedQuestion && !loading" class="no-data fade-in">
      <p>⚠️ Aucune donnée ne correspond à ces filtres.</p>
    </div>

  </div>
</template>

<style scoped>
/* REPRISE DU STYLE EXISTANT */
.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  color: white;
  font-family: 'Segoe UI', sans-serif;
  min-height: 100vh;
}

.stats-header {
  text-align: center;
  margin-bottom: 2rem;
}

.stats-header h1 {
  font-size: 2.5rem;
  font-weight: 300;
}

.highlight {
  color: #D4AF37;
  font-weight: 700;
}

.subtitle {
  color: #aaa;
  font-size: 1.1rem;
}

/* PANEL */
.control-panel {
  background: #2a2245;
  padding: 1.5rem;
  border-radius: 12px;
  border: 1px solid #444;
  margin-bottom: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.control-row.main-select {
  margin-bottom: 1rem;
}

/* BOUTON TOGGLE */
.toggle-row {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem; /* Espace entre les boutons */
  margin-bottom: 1rem;
}

.btn-toggle-filters {
  background: transparent;
  color: #D4AF37;
  border: 1px solid #D4AF37;
  padding: 8px 24px;
  border-radius: 50px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
}

.btn-toggle-filters:hover {
  background: rgba(212, 175, 55, 0.1);
  transform: translateY(-2px);
}

.btn-toggle-filters.active {
  background: #D4AF37;
  color: #1b1336;
}

.btn-reset {
  background: rgba(255, 107, 107, 0.1);
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  padding: 10px 18px;
  border-radius: 50px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.btn-reset:hover {
  background: #ff6b6b;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
}

/* Petit ajustement svg pour l'alignement */
.btn-reset svg {
  flex-shrink: 0;
}

/* ANIMATION SLIDE POUR LES FILTRES */
.filters-container {
  overflow: hidden;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 1.5rem;
  margin-top: 0.5rem;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

/* Vue Transition : Slide Fade */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.4s ease-out;
  max-height: 300px;
  opacity: 1;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
  padding-top: 0;
  margin-top: 0;
}

/* --- INPUTS & SELECTEURS --- */
label {
  display: block;
  color: #D4AF37;
  font-weight: bold;
  font-size: 0.9rem;
  text-transform: uppercase;
  margin-bottom: 0.5rem;
}

.select-wrapper {
  position: relative;
}

.arrow-icon {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #D4AF37;
  pointer-events: none;
}

select {
  width: 100%;
  padding: 0.8rem;
  border-radius: 8px;
  background-color: #1b1336;
  color: white;
  border: 1px solid #555;
  cursor: pointer;
  font-size: 1rem;
  appearance: none;
  transition: all 0.3s;
}

select:hover {
  border-color: #D4AF37;
}

select:focus {
  outline: none;
  border-color: #D4AF37;
}

/* --- MAP & VISUALISATIONS --- */
.map-wrapper {
  position: relative;
  width: 100%;
  height: 600px;
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid #333;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.6);
}

.map {
  width: 100%;
  height: 100%;
  z-index: 1;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(27, 19, 54, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(212, 175, 55, 0.3);
  border-top-color: #D4AF37;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.map-legend {
  position: absolute;
  bottom: 30px;
  right: 30px;
  background: rgba(18, 12, 36, 0.9);
  padding: 10px 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 0.8rem;
  z-index: 500;
  border: 1px solid #444;
}

.gradient-bar {
  width: 120px;
  height: 8px;
  background: linear-gradient(to right, rgba(212, 175, 55, 0.2), rgba(212, 175, 55, 1));
  border-radius: 4px;
}

.charts-section {
  margin-top: 4rem;
  border-top: 1px solid #333;
  padding-top: 2rem;
}

.chart-container {
  height: 400px;
  background: #2a2245;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #333;
}

h2 {
  text-align: center;
  color: #D4AF37;
  margin-bottom: 2rem;
  font-weight: 300;
}

.no-data {
  text-align: center;
  padding: 2rem;
  color: #aaa;
  margin-top: 2rem;
  background: #2a2245;
  border-radius: 8px;
}

.error-msg {
  color: #ff6b6b;
  text-align: center;
  margin-bottom: 1rem;
  background: rgba(255, 107, 107, 0.1);
  padding: 1rem;
  border-radius: 8px;
}

.fade-in {
  animation: fadeIn 0.8s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
