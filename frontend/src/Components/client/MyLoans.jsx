import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { getMyLoans, applyForLoan, getMyAccounts } from '../../api/ClientService'
import DataTable from '../common/DataTable'

const MyLoans = () => {
  const [loans, setLoans] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchLoans()
  }, [currentPage, searchTerm])

  const fetchLoans = async () => {
    setLoading(true)
    try {
      const params = {
        page: currentPage,
        size: 5
      }
      if (searchTerm) {
        params.search = searchTerm
      }
      const response = await getMyLoans(params)
      setLoans(response.content || [])
      setTotalPages(response.totalPages || 0)
    } catch (error) {
      toast.error('Failed to fetch loans')
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
      key: 'amount',
      header: 'Loan Amount',
      render: (row) => `RWF ${row.amount?.toLocaleString() || '0'}`
    },
    {
      key: 'interestRate',
      header: 'Interest Rate',
      render: (row) => `${row.interestRate || 0}%`
    },
    {
      key: 'startDate',
      header: 'Start Date',
      render: (row) => row.startDate ? new Date(row.startDate).toLocaleDateString() : 'N/A'
    },
    {
      key: 'endDate',
      header: 'End Date',
      render: (row) => row.endDate ? new Date(row.endDate).toLocaleDateString() : 'N/A'
    },
    {
      key: 'loanStatus',
      header: 'Status',
      render: (row) => (
        <span className={`px-2 py-1 rounded text-xs font-semibold ${row.loanStatus === 'APPROVED' ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
          row.loanStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200' :
            'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
          }`}>
          {row.loanStatus}
        </span>
      )
    },
    {
      key: 'isRepaid',
      header: 'Repayment',
      render: (row) => (
        <span className={`px-2 py-1 rounded text-xs font-semibold ${row.isRepaid ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
          'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200'
          }`}>
          {row.isRepaid ? 'Repaid' : 'Outstanding'}
        </span>
      )
    }
  ]



  const [showLoanModal, setShowLoanModal] = useState(false)
  const [loanData, setLoanData] = useState({
    accountNumber: '',
    amount: 0,
    interestRate: 5.0,
    months: 12
  })
  const [accounts, setAccounts] = useState([])
  const [applying, setApplying] = useState(false)

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

  const handleApplyForLoan = async (e) => {
    e.preventDefault()
    setApplying(true)
    try {
      await applyForLoan(loanData)
      toast.success('Loan application submitted successfully!')
      setShowLoanModal(false)
      setLoanData({ accountNumber: '', amount: 0, interestRate: 5.0, months: 12 })
      fetchLoans()
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to apply for loan')
    } finally {
      setApplying(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center flex-wrap gap-4">
        <h1 className="text-3xl font-bold text-gray-800 dark:text-white">My Loans</h1>
        <button
          onClick={() => setShowLoanModal(true)}
          className="px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 transition font-medium"
        >
          + Apply for Loan
        </button>
      </div>

      <DataTable
        columns={columns}
        data={loans}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onSearch={handleSearch}
        searchPlaceholder="Search loans by amount, status, or date..."
        loading={loading}
      />

      {/* Apply for Loan Modal */}
      {showLoanModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-md">
            <h2 className="text-2xl font-bold mb-4 text-gray-800 dark:text-white">Apply for Loan</h2>
            <form onSubmit={handleApplyForLoan} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Account
                </label>
                <select
                  value={loanData.accountNumber}
                  onChange={(e) => setLoanData({ ...loanData, accountNumber: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                >
                  <option value="">Select Account</option>
                  {accounts.map(acc => (
                    <option key={acc.accountId} value={acc.accountNumber}>
                      {acc.accountNumber} - {acc.accountType}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Loan Amount (RWF)
                </label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={loanData.amount}
                  onChange={(e) => setLoanData({ ...loanData, amount: parseFloat(e.target.value) || 0 })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Interest Rate (%)
                </label>
                <input
                  type="number"
                  min="0"
                  max="100"
                  step="0.1"
                  value={loanData.interestRate}
                  onChange={(e) => setLoanData({ ...loanData, interestRate: parseFloat(e.target.value) || 0 })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Loan Duration (Months)
                </label>
                <input
                  type="number"
                  min="1"
                  max="120"
                  value={loanData.months}
                  onChange={(e) => setLoanData({ ...loanData, months: parseInt(e.target.value) || 12 })}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                  required
                />
              </div>
              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={applying}
                  className="flex-1 px-4 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 disabled:opacity-50"
                >
                  {applying ? 'Submitting...' : 'Apply for Loan'}
                </button>
                <button
                  type="button"
                  onClick={() => setShowLoanModal(false)}
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

export default MyLoans
