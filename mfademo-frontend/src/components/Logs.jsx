import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { logAPI } from '../services/api';
import './Logs.css';

const Logs = () => {
  const { user } = useAuth();
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAll, setShowAll] = useState(false);

  useEffect(() => {
    loadLogs();
  }, [showAll]);

  const loadLogs = async () => {
    try {
      setLoading(true);
      const data = showAll && user?.role === 'ADMIN' 
        ? await logAPI.getAllLogs()
        : await logAPI.getMyLogs();
      setLogs(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR');
  };

  if (loading) {
    return <div className="loading">Carregando logs...</div>;
  }

  if (error) {
    return <div className="error">Erro: {error}</div>;
  }

  return (
    <div className="logs-container">
      <div className="logs-header">
        <h1>Logs de Auditoria</h1>
        {user?.role === 'ADMIN' && (
          <button 
            onClick={() => setShowAll(!showAll)} 
            className="btn-toggle"
          >
            {showAll ? 'Ver Meus Logs' : 'Ver Todos os Logs'}
          </button>
        )}
      </div>

      {logs.length > 0 ? (
        <div className="logs-table">
          <table>
            <thead>
              <tr>
                <th>Data/Hora</th>
                <th>Usuário</th>
                <th>Endpoint</th>
                <th>Status</th>
                <th>Mensagem</th>
                <th>IP</th>
              </tr>
            </thead>
            <tbody>
              {logs.map((log) => (
                <tr key={log.id} className={log.success ? 'log-success' : 'log-failed'}>
                  <td>{formatDate(log.timestamp)}</td>
                  <td>{log.username}</td>
                  <td>{log.endpoint}</td>
                  <td>{log.success ? '✅ Sucesso' : '❌ Negado'}</td>
                  <td>{log.message}</td>
                  <td>{log.ipAddress || 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p>Nenhum log encontrado.</p>
      )}
    </div>
  );
};

export default Logs;

