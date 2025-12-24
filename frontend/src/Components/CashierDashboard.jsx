import { Link } from 'react-router-dom'

const CashierDashboard = () => {

  return (
    <div className="space-y-4 md:space-y-6">
      <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Cashier Dashboard</h1>

      {/* Business Information Summary */}
      <div className="bg-gradient-to-r from-[#004d4c] to-[#006b6a] rounded-lg shadow p-4 md:p-6 text-white">
        <h2 className="text-lg md:text-xl font-bold mb-2">Daily Operations Summary</h2>
        <p className="text-sm md:text-base opacity-90 mb-3">
          Track your daily transaction activities and performance metrics.
        </p>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 md:gap-4 text-sm">
          <div>
            <p className="opacity-75">Today's Transactions</p>
            <p className="text-lg md:text-xl font-bold">0</p>
          </div>
          <div>
            <p className="opacity-75">Total Amount Processed</p>
            <p className="text-lg md:text-xl font-bold">RWF 0</p>
          </div>
          <div>
            <p className="opacity-75">Pending Approvals</p>
            <p className="text-lg md:text-xl font-bold">0</p>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Today's Transactions</h3>
          <p className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white mt-2">0</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Total Amount</h3>
          <p className="text-2xl md:text-3xl font-bold text-green-600 dark:text-green-400 mt-2">RWF 0</p>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
          <h3 className="text-gray-500 dark:text-gray-400 text-sm font-medium">Pending Approvals</h3>
          <p className="text-2xl md:text-3xl font-bold text-yellow-600 dark:text-yellow-400 mt-2">0</p>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <Link to="/cashier/transaction" className="block p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left">
            <div className="text-2xl mb-2">💵</div>
            <h3 className="font-semibold">Process Deposit</h3>
            <p className="text-sm opacity-75">Handle customer deposits</p>
          </Link>

          <Link to="/cashier/transaction" className="block p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left">
            <div className="text-2xl mb-2">💸</div>
            <h3 className="font-semibold">Process Withdrawal</h3>
            <p className="text-sm opacity-75">Handle customer withdrawals</p>
          </Link>

          <button className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left">
            <div className="text-2xl mb-2">🔍</div>
            <h3 className="font-semibold">Account Lookup</h3>
            <p className="text-sm opacity-75">Search customer accounts</p>
          </button>

          <button className="p-4 border-2 border-[#004d4c] dark:border-[#006b6a] rounded-lg hover:bg-[#004d4c] hover:text-white dark:hover:bg-[#006b6a] transition text-left">
            <div className="text-2xl mb-2">📋</div>
            <h3 className="font-semibold">View Report</h3>
            <p className="text-sm opacity-75">Check daily transactions</p>
          </button>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
        <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">Recent Activity</h2>
        <div className="text-center text-gray-500 dark:text-gray-400 py-8">
          No recent transactions
        </div>
      </div>
    </div>
  )
}

export default CashierDashboard
