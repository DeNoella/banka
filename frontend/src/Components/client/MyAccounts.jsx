import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { getMyAccounts, createAccount } from '../../api/ClientService'
import DataTable from '../common/DataTable'

const MyAccounts = () => {
  const [accounts, setAccounts] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchAccounts()
  }, [currentPage, searchTerm])

  const fetchAccounts = async () => {
    setLoading(true)
    try {
      const params = {
        page: currentPage,
        size: 5
      }
      if (searchTerm) {
        params.search = searchTerm
      }
      const response = await getMyAccounts(params)
      setAccounts(response.content || [])
      setTotalPages(response.totalPages || 0)
    } catch (error) {
      toast.error('Failed to fetch accounts')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = (term) => {
    setSearchTerm(term)
    setCurrentPage(0)
  }

  const columns = [
    {
      key: 'accountNumber',
      header: 'Account Number'
    },
    {
      key: 'accountType',
      header: 'Type',
      render: (row) => (
        <span className="px-2 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 rounded text-xs font-semibold">
          {row.accountType}
        </span>
      )
    },
    {
      key: 'balance',
      header: 'Balance',
      render: (row) => (
        <span className="font-semibold text-green-600 dark:text-green-400">
          RWF {row.balance?.toLocaleString() || '0'}
        </span>
      )
    },
    {
      key: 'status',
      header: 'Status',
      render: (row) => (
        <div className="flex gap-2">
          <span className={`px-2 py-1 rounded text-xs font-semibold ${row.verified ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
            'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
            }`}>
            {row.verified ? 'Verified' : 'Pending'}
          </span>
          <span className={`px-2 py-1 rounded text-xs font-semibold ${row.active ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
            'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
            }`}>
            {row.active ? 'Active' : 'Inactive'}
          </span>
        </div>
      )
    }
  ]



  const [showCreateModal, setShowCreateModal] = useState(false)
  const [formData, setFormData] = useState({
    accountType: 'SAVING',
    balance: 0
  })
  const [creating, setCreating] = useState(false)

  const handleCreateAccount = async (e) => {
    e.preventDefault()
    setCreating(true)
    try {
      await createAccount(formData)
      toast.success('Account created successfully!')
      setShowCreateModal(false)
      setFormData({ accountType: 'SAVING', balance: 0 })
      fetchAccounts()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to create account')
    } finally {
      setCreating(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center flex-wrap gap-4">
        <h1 className="text-3xl font-bold text-gray-800 dark:text-white">My Accounts</h1>
        <button
          onClick={() => setShowCreateModal(true)}
          className="px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 transition font-medium"
        >
          + Create Account
        </button>
      </div>

      <DataTable
        columns={columns}
        data={accounts}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onSearch={handleSearch}
        searchPlaceholder="Search accounts by number or type..."
        loading={loading}
      />

      {/* Create Account Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-md">
            <h2 className="text-2xl font-bold mb-4 text-gray-800 dark:text-white">Create New Account</h2>
            <form onSubmit={handleCreateAccount} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Account Type
                </label>
                <select
                  value={formData.accountType}
                  onChange={(e) => setFormData({ ...formData, accountType: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                >
                  <option value="SAVING">Savings Account</option>
                  <option value="CHECKING">Checking Account</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Initial Balance (RWF)
                </label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={formData.balance}
                  onChange={(e) => setFormData({ ...formData, balance: parseFloat(e.target.value) || 0 })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={creating}
                  className="flex-1 px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 disabled:opacity-50"
                >
                  {creating ? 'Creating...' : 'Create Account'}
                </button>
                <button
                  type="button"
                  onClick={() => setShowCreateModal(false)}
                  className="flex-1 px-4 py-2 bg-gray-300 dark:bg-gray-600 text-gray-800 dark:text-white rounded-lg hover:opacity-90"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default MyAccounts
