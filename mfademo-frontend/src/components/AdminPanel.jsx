import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { adminAPI } from '../services/api';
import './AdminPanel.css';

const AdminPanel = () => {
  const { user } = useAuth();
  const [panelData, setPanelData] = useState(null);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPanel();
    loadUsers();
  }, []);

  const loadPanel = async () => {
    try {
      const data = await adminAPI.getPanel();
      setPanelData(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await adminAPI.getUsers();
      setUsers(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Carregando painel admin...</div>;
  }

  if (error) {
    return <div className="error">Erro: {error}</div>;
  }

  return (
    <div className="admin-container">
      <h1>Painel Administrativo</h1>
      
      {panelData && (
        <div className="info-card">
          <h2>Funcionalidades</h2>
          <ul>
            {panelData.features?.map((feature, index) => (
              <li key={index}>{feature}</li>
            ))}
          </ul>
        </div>
      )}

      <div className="users-section">
        <h2>Usuários do Sistema</h2>
        {users.length > 0 ? (
          <div className="users-table">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Usuário</th>
                  <th>Role</th>
                  <th>Email</th>
                  <th>Status</th>
                  <th>MFA</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.id}</td>
                    <td>{u.username}</td>
                    <td>
                      <span className={`role-badge role-${u.role.toLowerCase()}`}>
                        {u.role}
                      </span>
                    </td>
                    <td>{u.email || 'N/A'}</td>
                    <td>{u.enabled ? '✅' : '❌'}</td>
                    <td>{u.mfaEnabled ? '✅' : '❌'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p>Nenhum usuário encontrado.</p>
        )}
      </div>
    </div>
  );
};

export default AdminPanel;

