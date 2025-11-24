const API_BASE_URL = 'http://localhost:8080';

// Função auxiliar para fazer requisições
async function request(endpoint, options = {}) {
  const token = localStorage.getItem('token');
  
  const config = {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` }),
      ...options.headers,
    },
  };

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.message || data.error || 'Erro na requisição');
    }
    
    return data;
  } catch (error) {
    throw error;
  }
}

// API de Autenticação
export const authAPI = {
  login: async (username, password) => {
    return request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
  },

  verifyMfa: async (tmpToken, code) => {
    return request('/auth/mfa/verify', {
      method: 'POST',
      body: JSON.stringify({ tmpToken, code }),
    });
  },
};

// API de Usuário
export const userAPI = {
  getProfile: async () => {
    return request('/api/user/profile');
  },

  getDashboard: async () => {
    return request('/api/user/dashboard');
  },
};

// API de Manager
export const managerAPI = {
  getReports: async () => {
    return request('/api/manager/reports');
  },

  getDashboard: async () => {
    return request('/api/manager/dashboard');
  },
};

// API de Admin
export const adminAPI = {
  getPanel: async () => {
    return request('/api/admin/panel');
  },

  getUsers: async () => {
    return request('/api/admin/users');
  },
};

// API de Logs
export const logAPI = {
  getAllLogs: async () => {
    return request('/api/logs');
  },

  getMyLogs: async () => {
    return request('/api/logs/my');
  },
};

