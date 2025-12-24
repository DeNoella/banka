import { useState, useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { createManager, getStats } from '../api/AdminService'
import { toast } from 'react-toastify'

const AdminDashboard = () => {
  const location = useLocation()
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [email, setEmail] = useState('')
  const [phoneNumber, setPhoneNumber] = useState('')
  const [loading, setLoading] = useState(false)
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalAccounts: 0,
    activeLoans: 0
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

  const handleCreateManager = async (e) => {
    e.preventDefault()

    if (!firstName || !lastName || !email || !phoneNumber) {
      toast.error('All fields are required')
      return
    }

    setLoading(true)
    try {
      await createManager({
        firstName,
        lastName,
        email,
        phoneNumber
      })

      toast.success('Manager created successfully!')

      setFirstName('')
      setLastName('')
      setEmail('')
      setPhoneNumber('')
      
      // Refresh stats after creating manager
      await fetchStats()
    } catch (error) {
      if (error.response?.status === 403) {
        toast.error('Only admin can create manager')
      } else {
        toast.error(error.response?.data?.message || 'Failed to create manager')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-4 md:space-y-6">
      <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Admin Dashboard</h1>

      {/* Business Information Summary */}
      <div className="bg-gradient-to-r from-[#004d4c] to-[#006b6a] rounded-lg shadow p-4 md:p-6 text-white">
        <h2 className="text-lg md:text-xl font-bold mb-2">Bank of Citizens - Business Summary</h2>
        <p className="text-sm md:text-base opacity-90 mb-3">
          Comprehensive overview of the banking system's performance and operations.
        </p>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 md:gap-4 text-sm">
          <div>
            <p className="opacity-75">Total System Users</p>
            <p className="text-lg md:text-xl font-bold">{stats.totalUsers}</p>
          </div>
          <div>
            <p className="opacity-75">Total Accounts</p>
            <p className="text-lg md:text-xl font-bold">{stats.totalAccounts}</p>
          </div>
          <div>
            <p className="opacity-75">Active Loans</p>
            <p className="text-lg md:text-xl font-bold">{stats.activeLoans}</p>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Users</h3>
          <p className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white mt-2">{stats.totalUsers}</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Accounts</h3>
          <p className="text-2xl md:text-3xl font-bold text-green-600 dark:text-green-400 mt-2">{stats.totalAccounts}</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Active Loans</h3>
          <p className="text-2xl md:text-3xl font-bold text-yellow-600 dark:text-yellow-400 mt-2">{stats.activeLoans}</p>
        </div>
      </div>

      {/* Create Manager Form */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Create Manager</h2>

        <form onSubmit={handleCreateManager} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                First Name
              </label>
              <input
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                className="w-full p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Last Name
              </label>
              <input
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                className="w-full p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Email
              </label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Phone Number
              </label>
              <input
                type="tel"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                className="w-full p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full md:w-auto px-6 py-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg font-medium hover:opacity-90 transition disabled:opacity-50"
          >
            {loading ? 'Creating...' : 'Create Manager'}
          </button>
        </form>
      </div>

      {/* Quick Links */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Quick Actions</h2>
        <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 gap-3 md:gap-4">
          <a
            href="/admin/users"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-center"
          >
            <div className="text-2xl mb-2">👥</div>
            <h3 className="font-semibold">Manage Users</h3>
          </a>

          <a
            href="/admin/accounts"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-center"
          >
            <div className="text-2xl mb-2">💳</div>
            <h3 className="font-semibold">View Accounts</h3>
          </a>

          <a
            href="/admin/loans"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-center"
          >
            <div className="text-2xl mb-2">💰</div>
            <h3 className="font-semibold">Manage Loans</h3>
          </a>

          <a
            href="/admin/search"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-center"
          >
            <div className="text-2xl mb-2">🔍</div>
            <h3 className="font-semibold">Global Search</h3>
          </a>
        </div>
      </div>
    </div>
  )
}

export default AdminDashboard