import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { managerAPI } from '../services/api';
import './Reports.css';

const Reports = () => {
  const { user } = useAuth();
  const [reports, setReports] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadReports();
  }, []);

  const loadReports = async () => {
    try {
      setLoading(true);
      const data = await managerAPI.getReports();
      setReports(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Carregando relatórios...</div>;
  }

  if (error) {
    return <div className="error">Erro: {error}</div>;
  }

  return (
    <div className="reports-container">
      <h1>Relatórios</h1>
      
      {reports && (
        <>
          <div className="info-card">
            <p><strong>Usuário:</strong> {reports.username}</p>
            <p><strong>Role:</strong> {reports.role}</p>
            <p><strong>Mensagem:</strong> {reports.message}</p>
          </div>

          <div className="reports-list">
            <h2>Relatórios Disponíveis</h2>
            {reports.reports && reports.reports.length > 0 ? (
              <div className="reports-grid">
                {reports.reports.map((report) => (
                  <div key={report.id} className="report-card">
                    <h3>{report.name}</h3>
                    <p>Status: {report.status}</p>
                    <p>ID: {report.id}</p>
                  </div>
                ))}
              </div>
            ) : (
              <p>Nenhum relatório disponível.</p>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default Reports;

