import { useState } from 'react'
import { toast } from 'react-toastify'
import axios from '../../api/axios'

const ProcessTransaction = () => {
    const [transactionType, setTransactionType] = useState('DEPOSIT')
    const [accountNumber, setAccountNumber] = useState('')
    const [amount, setAmount] = useState('')
    const [loading, setLoading] = useState(false)
    const [accountDetails, setAccountDetails] = useState(null)

    const handleCheckAccount = async () => {
        if (!accountNumber) return

        try {
            // Need an endpoint to get account details by number
            // Assuming GET /account/{accountNumber} or searching via existing endpoint
            // For now, let's just assume we proceed. In a real app, we'd verify the name.
            const response = await axios.get(`/admin/search?query=${accountNumber}`)
            if (response.data && response.data.length > 0) {
                // Logic to confirm account owner would go here
                toast.info(`Account found`)
            }
        } catch (error) {
            // silent fail for now or toast
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)

        try {
            const endpoint = transactionType === 'DEPOSIT'
                ? '/transaction/deposit'
                : '/transaction/withdraw'

            await axios.post(endpoint, {
                accountNumber,
                amount: parseFloat(amount)
            })

            toast.success(`${transactionType} successful!`)
            setAmount('')
            setAccountNumber('')
        } catch (error) {
            toast.error(error.response?.data?.message || 'Transaction failed')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="max-w-2xl mx-auto space-y-4 md:space-y-6">
            <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Process Transaction</h1>

            <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
                {/* Transaction Type Tabs */}
                <div className="flex mb-4 md:mb-6 bg-gray-100 dark:bg-gray-700 rounded-lg p-1">
                    <button
                        onClick={() => setTransactionType('DEPOSIT')}
                        className={`flex-1 py-2 sm:py-2.5 rounded-md text-sm sm:text-base font-medium transition ${transactionType === 'DEPOSIT'
                                ? 'bg-white dark:bg-gray-600 shadow text-[#004d4c] dark:text-white'
                                : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300'
                            }`}
                    >
                        Deposit
                    </button>
                    <button
                        onClick={() => setTransactionType('WITHDRAW')}
                        className={`flex-1 py-2 sm:py-2.5 rounded-md text-sm sm:text-base font-medium transition ${transactionType === 'WITHDRAW'
                                ? 'bg-white dark:bg-gray-600 shadow text-[#004d4c] dark:text-white'
                                : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-300'
                            }`}
                    >
                        Withdraw
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                            Account Number
                        </label>
                        <input
                            type="text"
                            value={accountNumber}
                            onChange={(e) => setAccountNumber(e.target.value)}
                            onBlur={handleCheckAccount}
                            className="w-full px-3 sm:px-4 py-2 text-sm sm:text-base border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                            Amount (RWF)
                        </label>
                        <input
                            type="number"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                            min="100"
                            className="w-full px-3 sm:px-4 py-2 text-sm sm:text-base border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className={`w-full px-4 sm:px-6 py-2 sm:py-3 text-sm sm:text-base text-white rounded-lg hover:opacity-90 disabled:opacity-50 font-medium ${transactionType === 'DEPOSIT'
                                ? 'bg-[#004d4c] dark:bg-[#006b6a]'
                                : 'bg-red-600 hover:bg-red-700'
                            }`}
                    >
                        {loading ? 'Processing...' : `Confirm ${transactionType}`}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default ProcessTransaction
