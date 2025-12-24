import { useState, useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { getStats } from '../api/ManagerService'
import { toast } from 'react-toastify'

const ManagerDashboard = () => {
  const location = useLocation()
  const [stats, setStats] = useState({
    totalClients: 0,
    totalCashiers: 0
  })

  useEffect(() => {
    fetchStats()
  }, [location.pathname]) // Refresh when navigating back to dashboard

  const fetchStats = async () => {
    try {
      const data = await getStats()
      setStats(data)
    } catch (error) {
      console.error('Failed to fetch stats:', error)
      toast.error('Failed to load dashboard statistics')
    }
  }

  return (
    <div className="space-y-4 md:space-y-6">
      <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Manager Dashboard</h1>

      {/* Business Information Summary */}
      <div className="bg-gradient-to-r from-[#004d4c] to-[#006b6a] rounded-lg shadow p-4 md:p-6 text-white">
        <h2 className="text-lg md:text-xl font-bold mb-2">Branch Management Summary</h2>
        <p className="text-sm md:text-base opacity-90 mb-3">
          Overview of clients and staff under your management.
        </p>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 md:gap-4 text-sm">
          <div>
            <p className="opacity-75">Total Clients</p>
            <p className="text-lg md:text-xl font-bold">{stats.totalClients}</p>
          </div>
          <div>
            <p className="opacity-75">Total Cashiers</p>
            <p className="text-lg md:text-xl font-bold">{stats.totalCashiers}</p>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 md:gap-6">
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Clients</h3>
          <p className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white mt-2">{stats.totalClients}</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Cashiers</h3>
          <p className="text-2xl md:text-3xl font-bold text-green-600 dark:text-green-400 mt-2">{stats.totalCashiers}</p>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <a
            href="/manager/create-client"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left"
          >
            <div className="text-2xl mb-2">👤</div>
            <h3 className="font-semibold">Create New Client</h3>
            <p className="text-sm opacity-75">Register a new client with location details</p>
          </a>

          <a
            href="/manager/create-cashier"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left"
          >
            <div className="text-2xl mb-2">💼</div>
            <h3 className="font-semibold">Create New Cashier</h3>
            <p className="text-sm opacity-75">Add a new cashier to the system</p>
          </a>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-4">Recent Activity</h2>
        <div className="text-center text-gray-500 dark:text-gray-400 py-8">
          No recent activity
        </div>
      </div>
    </div>
  )
}

export default ManagerDashboard
