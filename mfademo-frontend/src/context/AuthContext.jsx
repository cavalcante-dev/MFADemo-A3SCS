import { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar se há token salvo
    const savedToken = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    if (savedToken && savedUser) {
      setToken(savedToken);
      setUser(JSON.parse(savedUser));
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      const response = await authAPI.login(username, password);
      
      if (response.mfaRequired) {
        // Salvar temporariamente username e role para usar após MFA
        const tempUserData = {
          username,
          role: response.role,
        };
        setUser(tempUserData);
        localStorage.setItem('tempUser', JSON.stringify(tempUserData));
        return {
          success: true,
          mfaRequired: true,
          tmpToken: response.tmpToken,
          role: response.role,
          username,
        };
      } else {
        const userData = {
          username,
          role: response.role,
        };
        setToken(response.token);
        setUser(userData);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));
        return {
          success: true,
          mfaRequired: false,
        };
      }
    } catch (error) {
      return {
        success: false,
        error: error.message,
      };
    }
  };

  const verifyMfa = async (tmpToken, code, username, role) => {
    try {
      const response = await authAPI.verifyMfa(tmpToken, code);
      const userData = {
        username: username || user?.username || 'user',
        role: role || user?.role || 'USER',
      };
      setToken(response.token);
      setUser(userData);
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.removeItem('tempUser');
      return {
        success: true,
      };
    } catch (error) {
      return {
        success: false,
        error: error.message,
      };
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };

  const value = {
    user,
    token,
    loading,
    login,
    verifyMfa,
    logout,
    isAuthenticated: !!token,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

