import { useState, useEffect } from 'react'
import { getMyAccounts, getMyLoans } from '../api/ClientService'
import { toast } from 'react-toastify'

const BankUserDashboard = () => {
  const [stats, setStats] = useState({
    totalBalance: 0,
    activeAccounts: 0,
    activeLoans: 0
  })

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      // Fetch accounts to calculate balance and active count
      const accountsData = await getMyAccounts({ size: 100 }) // Fetch up to 100 to get a good estimate
      const accounts = accountsData.content || []

      const totalBalance = accounts.reduce((sum, acc) => sum + (acc.balance || 0), 0)
      const activeAccounts = accounts.filter(acc => acc.active).length

      // Fetch loans to get active loans count
      const loansData = await getMyLoans({ size: 100 })
      const loans = loansData.content || []
      const activeLoans = loans.filter(loan => loan.loanStatus === 'APPROVED' && !loan.isRepaid).length

      setStats({
        totalBalance,
        activeAccounts,
        activeLoans
      })
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error)
      // Don't toast error here to avoid annoyance on generic dashboard load, or keep it if critical
    }
  }

  return (
    <div className="space-y-4 md:space-y-6">
      <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Client Dashboard</h1>

      {/* Welcome Message */}
      <div className="bg-gradient-to-r from-[#004d4c] to-[#006b6a] rounded-lg shadow p-4 md:p-6 text-white">
        <h2 className="text-xl md:text-2xl font-bold mb-2">Welcome to BANKA</h2>
        <p className="opacity-90 text-sm md:text-base">Manage your accounts, track transactions, and apply for loans all in one place.</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Balance</h3>
          <p className="text-xl md:text-2xl lg:text-3xl font-bold text-green-600 dark:text-green-400 mt-2">
            RWF {stats.totalBalance.toLocaleString()}
          </p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Active Accounts</h3>
          <p className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white mt-2">{stats.activeAccounts}</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Active Loans</h3>
          <p className="text-2xl md:text-3xl font-bold text-yellow-600 dark:text-yellow-400 mt-2">{stats.activeLoans}</p>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          <a
            href="/client/accounts"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left"
          >
            <div className="text-2xl mb-2">💳</div>
            <h3 className="font-semibold">View Accounts</h3>
            <p className="text-sm opacity-75">Check your account balances</p>
          </a>

          <a
            href="/client/transactions"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left"
          >
            <div className="text-2xl mb-2">💸</div>
            <h3 className="font-semibold">View Transactions</h3>
            <p className="text-sm opacity-75">Review your transaction history</p>
          </a>

          <a
            href="/client/loans"
            className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left"
          >
            <div className="text-2xl mb-2">💰</div>
            <h3 className="font-semibold">My Loans</h3>
            <p className="text-sm opacity-75">Check your loan applications</p>
          </a>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <h2 className="text-xl font-bold text-gray-800 dark:text-white mb-4">Recent Activity</h2>
        <div className="text-center text-gray-500 dark:text-gray-400 py-8">
          No recent transactions
        </div>
      </div>
    </div>
  )
}

export default BankUserDashboard
