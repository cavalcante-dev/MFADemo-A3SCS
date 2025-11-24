import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { userAPI, managerAPI, adminAPI } from '../services/api';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      let data;
      
      if (user?.role === 'ADMIN') {
        data = await adminAPI.getPanel();
      } else if (user?.role === 'MANAGER') {
        data = await managerAPI.getDashboard();
      } else {
        data = await userAPI.getDashboard();
      }
      
      setDashboardData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Carregando...</div>;
  }

  if (error) {
    return <div className="error">Erro: {error}</div>;
  }

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div>
          <h1>Dashboard</h1>
          <p>Bem-vindo, {user?.username}!</p>
        </div>
        <button onClick={logout} className="btn-logout">
          Sair
        </button>
      </header>

      <div className="dashboard-content">
        <div className="info-card">
          <h2>Informações do Usuário</h2>
          <p><strong>Usuário:</strong> {dashboardData?.username || user?.username}</p>
          <p><strong>Role:</strong> {dashboardData?.role || user?.role}</p>
          <p><strong>Mensagem:</strong> {dashboardData?.message}</p>
        </div>

        {dashboardData?.permissions && (
          <div className="info-card">
            <h2>Permissões</h2>
            <ul>
              {Object.entries(dashboardData.permissions).map(([key, value]) => (
                <li key={key}>
                  {key}: {value ? '✅' : '❌'}
                </li>
              ))}
            </ul>
          </div>
        )}

        {dashboardData?.features && (
          <div className="info-card">
            <h2>Funcionalidades Disponíveis</h2>
            <ul>
              {dashboardData.features.map((feature, index) => (
                <li key={index}>{feature}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;

