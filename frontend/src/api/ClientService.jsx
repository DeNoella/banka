import api from "./axios";

export const getMyAccounts = async (params) => {
    const response = await api.get('/client/accounts', { params })
    return response.data
}

export const getMyLoans = async (params) => {
    const response = await api.get('/client/loans', { params })
    return response.data
}

export const getMyTransactions = async (params) => {
    const response = await api.get('/client/transactions', { params })
    return response.data
}

export const createAccount = async (accountData) => {
    const response = await api.post('/client/create-account', accountData)
    return response.data
}

export const createTransaction = async (transactionData) => {
    const response = await api.post('/client/create-transaction', transactionData)
    return response.data
}

export const applyForLoan = async (loanData) => {
    const response = await api.post('/client/apply-loan', loanData)
    return response.data
}
