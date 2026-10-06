import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './pages/Home';
import Administracao from './pages/Administracao';
import Usuario from './pages/Usuario';
import Requisicao from './pages/Requisicao';
import Autorizacoes from './pages/Autorizacoes';
import AutorizacaoDetalhe from './pages/AutorizacaoDetalhe';

function App() {
  return (
    <BrowserRouter>
      <div className="flex min-h-screen flex-col bg-slate-50">
        <Navbar />
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/administracao" element={<Administracao />} />
            <Route path="/usuario" element={<Usuario />} />
            <Route path="/requisicao" element={<Requisicao />} />
            <Route path="/autorizacoes" element={<Autorizacoes />} />
            <Route path="/autorizacao/:id" element={<AutorizacaoDetalhe />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
