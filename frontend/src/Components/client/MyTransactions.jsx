import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { getMyTransactions, createTransaction, getMyAccounts } from '../../api/ClientService'
import DataTable from '../common/DataTable'

const MyTransactions = () => {
  const [transactions, setTransactions] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchTransactions()
  }, [currentPage, searchTerm])

  const fetchTransactions = async () => {
    setLoading(true)
    try {
      const params = {
        page: currentPage,
        size: 5
      }
      if (searchTerm) {
        params.search = searchTerm
      }
      const response = await getMyTransactions(params)
      setTransactions(response.content || [])
      setTotalPages(response.totalPages || 0)
    } catch (error) {
      toast.error('Failed to fetch transactions')
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
      key: 'transactionId',
      header: 'Transaction ID',
      render: (row) => row.transactionId?.substring(0, 8) || 'N/A'
    },
    {
      key: 'amount',
      header: 'Amount',
      render: (row) => (
        <span className={`font-semibold ${row.amount >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'
          }`}>
          {row.amount >= 0 ? '+' : ''}RWF {row.amount?.toLocaleString() || '0'}
        </span>
      )
    },
    {
      key: 'accountNumber',
      header: 'Account',
      render: (row) => row.accountNumber || 'N/A'
    },
    {
      key: 'timestamp',
      header: 'Date',
      render: (row) => row.timestamp ?
        new Date(row.timestamp).toLocaleString() : 'N/A'
    }
  ]



  const [showTransactionModal, setShowTransactionModal] = useState(false)
  const [transactionData, setTransactionData] = useState({
    accountNumber: '',
    amount: 0
  })
  const [accounts, setAccounts] = useState([])
  const [processing, setProcessing] = useState(false)

  useEffect(() => {
    fetchAccountsList()
  }, [])

  const fetchAccountsList = async () => {
    try {
      const response = await getMyAccounts({ size: 100 })
      setAccounts(response.content || [])
    } catch (error) {
      console.error('Failed to fetch accounts:', error)
    }
  }

  const handleCreateTransaction = async (e) => {
    e.preventDefault()
    setProcessing(true)
    try {
      await createTransaction(transactionData)
      toast.success('Transaction completed successfully!')
      setShowTransactionModal(false)
      setTransactionData({ accountNumber: '', amount: 0 })
      fetchTransactions()
      fetchAccountsList() // Refresh accounts to update balance
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to create transaction')
    } finally {
      setProcessing(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center flex-wrap gap-4">
        <h1 className="text-3xl font-bold text-gray-800 dark:text-white">My Transactions</h1>
        <button
          onClick={() => setShowTransactionModal(true)}
          className="px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 transition font-medium"
        >
          + Make Transaction
        </button>
      </div>

      <DataTable
        columns={columns}
        data={transactions}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onSearch={handleSearch}
        searchPlaceholder="Search transactions by amount, account, or date..."
        loading={loading}
      />

      {/* Create Transaction Modal */}
      {showTransactionModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-md">
            <h2 className="text-2xl font-bold mb-4 text-gray-800 dark:text-white">Make Transaction</h2>
            <form onSubmit={handleCreateTransaction} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Account
                </label>
                <select
                  value={transactionData.accountNumber}
                  onChange={(e) => setTransactionData({ ...transactionData, accountNumber: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                >
                  <option value="">Select Account</option>
                  {accounts.map(acc => (
                    <option key={acc.accountId} value={acc.accountNumber}>
                      {acc.accountNumber} - {acc.accountType} (Balance: RWF {acc.balance?.toLocaleString() || '0'})
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Amount (RWF) - Use negative for withdrawal
                </label>
                <input
                  type="number"
                  step="0.01"
                  value={transactionData.amount}
                  onChange={(e) => setTransactionData({ ...transactionData, amount: parseFloat(e.target.value) || 0 })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={processing}
                  className="flex-1 px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 disabled:opacity-50"
                >
                  {processing ? 'Processing...' : 'Submit Transaction'}
                </button>
                <button
                  type="button"
                  onClick={() => setShowTransactionModal(false)}
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

export default MyTransactions
