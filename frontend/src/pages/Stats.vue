<script setup lang="ts">
import { ref, onMounted, nextTick, watch, computed } from 'vue'
import axios from 'axios'
import * as L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import StatsChart from "@/components/StatsChart.vue"
import { getColorForText } from '@/assets/colors'

const API_URL = '/api'

const questions = ref<any[]>([])
const selectedQuestionId = ref<number | null>(null)
const stats = ref<any[]>([])
const loading = ref(true)
const error = ref('')

const showFilters = ref(false)
const filters = ref({
  gender: '',
  category: '',
  ageRange: ''
})

let map: L.Map | null = null
let geoLayer: L.GeoJSON<any> | null = null

const currentQuestion = computed(() => {
  return questions.value.find(q => q.id === selectedQuestionId.value)
})

const hasActiveFilters = computed(() => {
  return filters.value.gender !== '' || filters.value.category !== '' || filters.value.ageRange !== ''
})

const loadQuestions = async () => {
  try {
    // 👇 CHANGEMENT ICI : Utilisation de API_URL
    const res = await axios.get(`${API_URL}/questions`)
    questions.value = res.data
  } catch (err) {
    console.error(err)
    error.value = "Impossible de charger les questions."
  }
}

const loadStats = async () => {
  if (!selectedQuestionId.value) return
  loading.value = true
  error.value = ''

  try {
    let minAge = null, maxAge = null
    if (filters.value.ageRange) {
      const [min, max] = filters.value.ageRange.split('-')
      minAge = min
      maxAge = max
    }

    const response = await axios.get(`${API_URL}/responses/stats/${selectedQuestionId.value}`, {
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

const resetFilters = () => {
  filters.value = { gender: '', category: '', ageRange: '' }
}

const initMap = () => {
  const mapContainer = document.getElementById('map')
  if (!mapContainer) return
  if (map) return;

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
    .then(res => res.json())
    .then(data => {
      if (geoLayer) {
        geoLayer.addData(data)
        updateMapColors()
      }
    })
}

const updateMapColors = () => {
  if (!geoLayer || !currentQuestion.value) return

  geoLayer.eachLayer((layer: any) => {
    layer.setStyle({
      fillColor: '#333',
      fillOpacity: 0.3,
      color: '#555',
      weight: 1
    })
    layer.unbindTooltip()
    if (layer.feature.properties.NAME) layer.bindTooltip(layer.feature.properties.NAME)
  })

  if (currentQuestion.value.type === 'TEXT') return
  if (!stats.value.length) return

  geoLayer.eachLayer((layer: any) => {
    const props = layer.feature.properties
    const geoName = props.NAME || props.name || props.ADMIN

    const countryStat = stats.value.find((s: any) =>
      s.country.toLowerCase() === geoName.toLowerCase()
    )

    if (countryStat && countryStat.counts) {
      const counts = countryStat.counts
      const total = Object.values(counts).reduce((a: any, b: any) => a + b, 0) as number

      let color = '#D4AF37'
      let tooltipContent = `<strong>${geoName}</strong><br/>`

      if (currentQuestion.value.type === 'SCORE') {
        let sum = 0
        Object.entries(counts).forEach(([score, count]) => {
          sum += Number(score) * (count as number)
        })
        const average = total > 0 ? sum / total : 0

        const hue = ((average - 1) / 9) * 120
        color = `hsl(${hue}, 70%, 45%)`
        tooltipContent += `Moyenne : <strong>${average.toFixed(1)} / 10</strong><br/><small>${total} avis</small>`
      }

      else if (currentQuestion.value.type === 'CHOICE' || currentQuestion.value.type === 'MULTIPLE_CHOICE') {
        let maxCount = 0
        let dominantAnswer = ''

        Object.entries(counts).forEach(([answer, count]) => {
          const val = count as number
          if (val > maxCount) {
            maxCount = val
            dominantAnswer = answer
          }
        })

        const percent = Math.round((maxCount / total) * 100)
        color = getColorForText(dominantAnswer)
        tooltipContent += `Majorité : <span style="color:${color}">●</span> <strong>${dominantAnswer}</strong> (${percent}%)<br/><small>${total} avis</small>`
      }

      layer.setStyle({ fillColor: color, fillOpacity: 0.7, weight: 1, color: 'white' })
      layer.bindTooltip(`<div style="text-align: center;">${tooltipContent}</div>`)
    }
  })
}

const getLegendColor = (answerText: string) => getColorForText(answerText)

onMounted(async () => {
  await loadQuestions()
  loading.value = false
  await nextTick()
  initMap()
})

watch([selectedQuestionId, filters], async ([newQ]) => {
  if (newQ) await loadStats()
}, { deep: true })

const wordCloudData = computed(() => {
  if (currentQuestion.value?.type !== 'TEXT' || !stats.value.length) return []
  const totalCounts: Record<string, number> = {}
  let maxFreq = 1

  stats.value.forEach(stat => {
    if (!stat.counts) return
    Object.entries(stat.counts).forEach(([word, count]) => {
      const cleanWord = word.trim()
      if (cleanWord.length > 2) {
        const val = Number(count)
        totalCounts[cleanWord] = (totalCounts[cleanWord] || 0) + val
        if (totalCounts[cleanWord] > maxFreq) maxFreq = totalCounts[cleanWord]
      }
    })
  })

  return Object.entries(totalCounts)
    .map(([text, value]) => ({
      text, value,
      size: 1 + ((value / maxFreq) * 3)
    }))
    .sort((a, b) => b.value - a.value)
    .slice(0, 50)
})

const shouldShowChart = computed(() => {
  if (!stats.value.length) return false
  if (!selectedQuestionId.value) return false

  const idsToHide = [2, 23, 24]
  if (idsToHide.includes(selectedQuestionId.value)) return false

  if (currentQuestion.value?.type === 'TEXT') return false

  return true
})

const getRandomColor = () => {
  const colors = ['#D4AF37', '#e6194b', '#3cb44b', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff']
  return colors[Math.floor(Math.random() * colors.length)]
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
          <select v-model="selectedQuestionId">
            <option :value="null" disabled>-- Choisir une question --</option>
            <option v-for="q in questions" :key="q.id" :value="q.id">
              {{ q.text }}
            </option>
          </select>
          <div class="arrow-icon">▼</div>
        </div>
      </div>

      <div v-if="selectedQuestionId" class="toggle-row fade-in">
        <button @click="showFilters = !showFilters" class="btn-toggle-filters" :class="{ 'active': showFilters }">
          <span>{{ showFilters ? 'Masquer les filtres' : 'Filtrer les résultats' }}</span>
        </button>
        <button v-if="hasActiveFilters" @click="resetFilters" class="btn-reset">Effacer</button>
      </div>

      <Transition name="slide-fade">
        <div v-if="selectedQuestionId && showFilters" class="filters-container">
          <div class="filters-grid">
            <div class="filter-group">
              <label>Genre</label>
              <select v-model="filters.gender"><option value="">Tous</option><option>Homme</option><option>Femme</option><option>Autre</option></select>
            </div>
            <div class="filter-group">
              <label>Âge</label>
              <select v-model="filters.ageRange"><option value="">Tous les âges</option><option value="15-25">15 - 25 ans</option><option value="26-40">26 - 40 ans</option><option value="41-60">41 - 60 ans</option><option value="61-99">61 ans et +</option></select>
            </div>
            <div class="filter-group">
              <label>Statut</label>
              <select v-model="filters.category"><option value="">Tous statuts</option><option>Étudiant(e)</option><option>Employé(e)</option><option>Cadre</option><option>Ouvrier(ère)</option><option>Retraité(e)</option><option>Sans emploi</option></select>
            </div>
          </div>
        </div>
      </Transition>
    </div>

    <div class="map-wrapper">
      <div v-if="loading" class="loading-overlay"><div class="spinner"></div></div>

      <div v-if="currentQuestion?.type === 'TEXT'" class="word-cloud-box fade-in">
        <div v-if="wordCloudData.length > 0" class="cloud-container">
          <span
            v-for="word in wordCloudData"
            :key="word.text"
            class="cloud-word"
            :style="{ fontSize: word.size + 'rem', color: getRandomColor() }"
            :title="word.value + ' occurrences'"
          >
            {{ word.text }}
          </span>
        </div>
        <div v-else class="empty-cloud">
          <p>Pas assez de données pour générer un nuage.</p>
        </div>
      </div>

      <div v-show="currentQuestion?.type !== 'TEXT'" id="map" class="map"></div>

      <div class="map-legend" v-if="selectedQuestionId && currentQuestion && currentQuestion.type !== 'TEXT'">

        <div v-if="currentQuestion.type === 'SCORE'" class="legend-content">
          <span>1 (Nul)</span>
          <div class="gradient-bar-score"></div>
          <span>10 (Top)</span>
        </div>

        <div v-else-if="currentQuestion.type === 'CHOICE' || currentQuestion.type === 'MULTIPLE_CHOICE'" class="legend-choices">
          <div v-for="opt in currentQuestion.possibleAnswers.split(';')" :key="opt" class="legend-item">
            <span class="color-dot" :style="{ background: getLegendColor(opt) }"></span>
            <span>{{ opt }}</span>
          </div>
        </div>

      </div>
    </div>

    <div v-if="shouldShowChart" class="charts-section fade-in">
      <h2>Analyse détaillée</h2>
      <div class="chart-container">
        <StatsChart :stats="stats" :questionType="currentQuestion?.type" />
      </div>
    </div>

    <div v-else-if="selectedQuestionId && !loading && stats.length === 0" class="no-data fade-in">
      <p>⚠️ Aucune donnée ne correspond à ces filtres.</p>
    </div>

  </div>
</template>

<style scoped>
.stats-container { max-width: 1200px; margin: 0 auto; padding: 2rem; color: white; min-height: 100vh; }
.stats-header { text-align: center; margin-bottom: 2rem; }
.highlight { color: #D4AF37; font-weight: 700; }
.subtitle { color: #aaa; }

.map-wrapper { position: relative; width: 100%; height: 600px; border-radius: 16px; border: 1px solid #333; box-shadow: 0 10px 40px rgba(0,0,0,0.6); overflow: hidden; background: #222; }
.map { width: 100%; height: 100%; z-index: 1; }

.text-overlay {
  position: absolute; inset: 0;
  background: rgba(20, 20, 30, 0.85); backdrop-filter: blur(2px);
  z-index: 100;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  text-align: center; color: #fff;
}
.text-overlay p { font-size: 1.5rem; font-weight: bold; color: #D4AF37; margin-bottom: 0.5rem; }
.text-overlay span { color: #aaa; max-width: 400px; line-height: 1.5; }

.map-legend {
  position: absolute; bottom: 30px; right: 30px;
  background: rgba(18, 12, 36, 0.95);
  padding: 15px; border-radius: 8px; border: 1px solid #555;
  z-index: 500; max-width: 250px;
}

.legend-content { display: flex; align-items: center; gap: 10px; font-size: 0.8rem; }
.gradient-bar-score {
  width: 100px; height: 10px;
  background: linear-gradient(to right, #ff0000, #ffff00, #00ff00);
  border-radius: 4px;
}

.legend-choices { display: flex; flex-direction: column; gap: 5px; }
.legend-item { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; }
.color-dot { width: 12px; height: 12px; border-radius: 50%; display: block; }

.control-panel { background: #2a2245; padding: 1.5rem; border-radius: 12px; border: 1px solid #444; margin-bottom: 2rem; }
.loading-overlay { position: absolute; inset: 0; background: rgba(27,19,54,0.8); display: flex; align-items: center; justify-content: center; z-index: 10; }
.spinner { width: 40px; height: 40px; border: 4px solid rgba(212,175,55,0.3); border-top-color: #D4AF37; border-radius: 50%; animation: spin 1s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }
.slide-fade-enter-active, .slide-fade-leave-active { transition: all 0.3s ease; max-height: 200px; opacity: 1; }
.slide-fade-enter-from, .slide-fade-leave-to { max-height: 0; opacity: 0; }

select {
  width: 100%;
  padding: 0.8rem;
  padding-right: 2.5rem;
  border-radius: 8px;
  background: #1b1336;
  color: white;
  border: 1px solid #555;
  cursor: pointer;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.charts-section { margin-top: 3rem; }
.chart-container {
  background: #2a2245;
  padding: 1rem;
  border-radius: 16px;
  height: 500px;
  width: 100%;
  position: relative;
}
.no-data { text-align: center; padding: 2rem; color: #888; background: #2a2245; margin-top: 2rem; border-radius: 8px; }

.toggle-row { display: flex; justify-content: center; gap: 1rem; margin-bottom: 1rem; }
.btn-toggle-filters { background: transparent; color: #D4AF37; border: 1px solid #D4AF37; padding: 8px 24px; border-radius: 50px; cursor: pointer; font-weight: bold; transition: all 0.3s; }
.btn-toggle-filters:hover, .btn-toggle-filters.active { background: #D4AF37; color: #1b1336; }
.btn-reset { background: rgba(255,107,107,0.1); color: #ff6b6b; border: 1px solid #ff6b6b; padding: 8px 18px; border-radius: 50px; cursor: pointer; transition: all 0.3s; }
.btn-reset:hover { background: #ff6b6b; color: white; }

.filters-container { overflow: hidden; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 1.5rem; margin-top: 0.5rem; }
.filters-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; }
label { display: block; color: #D4AF37; font-weight: bold; font-size: 0.9rem; text-transform: uppercase; margin-bottom: 0.5rem; }
.select-wrapper { position: relative; }
.arrow-icon { position: absolute; right: 1rem; top: 50%; transform: translateY(-50%); color: #D4AF37; pointer-events: none; }

.word-cloud-box {
  width: 100%; height: 100%; background: #222; display: flex; align-items: center; justify-content: center; overflow: hidden; padding: 2rem;
}
.cloud-container { display: flex; flex-wrap: wrap; justify-content: center; align-items: center; gap: 1.5rem; max-width: 90%; }
.cloud-word { font-family: 'Segoe UI', sans-serif; font-weight: bold; cursor: default; transition: transform 0.3s ease, text-shadow 0.3s ease; line-height: 1; }
.cloud-word:hover { transform: scale(1.2); text-shadow: 0 0 15px rgba(255, 255, 255, 0.4); z-index: 10; }
.empty-cloud { color: #777; font-style: italic; }
.main-select {
  margin-bottom: 2rem;
}
</style>
