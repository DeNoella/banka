import { useState, useEffect } from 'react'
import axios from '../../api/axios'

const LocationCascade = ({ onLocationSelect, selectedLocationCode }) => {
  const [provinces, setProvinces] = useState([])
  const [districts, setDistricts] = useState([])
  const [sectors, setSectors] = useState([])
  const [cells, setCells] = useState([])
  const [villages, setVillages] = useState([])

  const [selectedProvince, setSelectedProvince] = useState('')
  const [selectedDistrict, setSelectedDistrict] = useState('')
  const [selectedSector, setSelectedSector] = useState('')
  const [selectedCell, setSelectedCell] = useState('')
  const [selectedVillage, setSelectedVillage] = useState('')

  // Fetch provinces on mount
  useEffect(() => {
    fetchProvinces()
  }, [])

  const fetchProvinces = async () => {
    try {
      const response = await axios.get('/location/provinces')
      setProvinces(response.data)
    } catch (error) {
      console.error('Error fetching provinces:', error)
    }
  }

  const fetchChildren = async (parentCode) => {
    try {
      const response = await axios.get(`/location/children?code=${parentCode}`)
      return response.data
    } catch (error) {
      console.error(`Error fetching children for ${parentCode}:`, error)
      return []
    }
  }

  const handleProvinceChange = async (e) => {
    const code = e.target.value
    setSelectedProvince(code)
    setSelectedDistrict('')
    setSelectedSector('')
    setSelectedCell('')
    setSelectedVillage('')
    setDistricts([])
    setSectors([])
    setCells([])
    setVillages([])

    if (code) {
      const children = await fetchChildren(code)
      setDistricts(children)
    }
  }

  const handleDistrictChange = async (e) => {
    const code = e.target.value
    setSelectedDistrict(code)
    setSelectedSector('')
    setSelectedCell('')
    setSelectedVillage('')
    setSectors([])
    setCells([])
    setVillages([])

    if (code) {
      const children = await fetchChildren(code)
      setSectors(children)
    }
  }

  const handleSectorChange = async (e) => {
    const code = e.target.value
    setSelectedSector(code)
    setSelectedCell('')
    setSelectedVillage('')
    setCells([])
    setVillages([])

    if (code) {
      const children = await fetchChildren(code)
      setCells(children)
    }
  }

  const handleCellChange = async (e) => {
    const code = e.target.value
    setSelectedCell(code)
    setSelectedVillage('')
    setVillages([])

    if (code) {
      const children = await fetchChildren(code)
      setVillages(children)
    }
  }

  const handleVillageChange = (e) => {
    const code = e.target.value
    setSelectedVillage(code)
    if (onLocationSelect) {
      onLocationSelect(code)
    }
  }

  return (
    <div className="space-y-4">
      {/* Province */}
      <div>
        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
          Province <span className="text-red-500">*</span>
        </label>
        <select
          value={selectedProvince}
          onChange={handleProvinceChange}
          className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
          required
        >
          <option value="">Select Province</option>
          {provinces.map((province) => (
            <option key={province.code} value={province.code}>
              {province.name}
            </option>
          ))}
        </select>
      </div>

      {/* District */}
      {selectedProvince && (
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
            District <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedDistrict}
            onChange={handleDistrictChange}
            className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
            required
          >
            <option value="">Select District</option>
            {districts.map((district) => (
              <option key={district.code} value={district.code}>
                {district.name}
              </option>
            ))}
          </select>
        </div>
      )}

      {/* Sector */}
      {selectedDistrict && (
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
            Sector <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedSector}
            onChange={handleSectorChange}
            className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
            required
          >
            <option value="">Select Sector</option>
            {sectors.map((sector) => (
              <option key={sector.code} value={sector.code}>
                {sector.name}
              </option>
            ))}
          </select>
        </div>
      )}

      {/* Cell */}
      {selectedSector && (
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
            Cell <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedCell}
            onChange={handleCellChange}
            className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
            required
          >
            <option value="">Select Cell</option>
            {cells.map((cell) => (
              <option key={cell.code} value={cell.code}>
                {cell.name}
              </option>
            ))}
          </select>
        </div>
      )}

      {/* Village */}
      {selectedCell && (
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
            Village <span className="text-red-500">*</span>
          </label>
          <select
            value={selectedVillage}
            onChange={handleVillageChange}
            className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
            required
          >
            <option value="">Select Village</option>
            {villages.map((village) => (
              <option key={village.code} value={village.code}>
                {village.name}
              </option>
            ))}
          </select>
        </div>
      )}
    </div>
  )
}

export default LocationCascade
