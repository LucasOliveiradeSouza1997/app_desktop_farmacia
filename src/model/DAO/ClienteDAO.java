package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ConnectionFactory.ConnectionFactory;
import Exception.ClienteNaoEncontradoException;
import model.bean.Cliente;

public class ClienteDAO {

	public void create(Cliente c) {

		Connection conexao = ConnectionFactory.getConnection();
		PreparedStatement ps = null;

		try {
			ps = conexao.prepareStatement("INSERT INTO cliente(cpfCliente,nomeCLiente,rgCLiente,enderecoCliente,telefoneCLiente,tipoCLiente)VALUES(?,?,?,?,?,?)");

			ps.setString(1,c.getCpfCliente());
			ps.setString(2,c.getNomeCLiente());
			ps.setString(3,c.getRgCLiente());
			ps.setString(4,c.getEnderecoCliente());
			ps.setString(5,c.getTelefoneCLiente());
			ps.setString(6,c.getTipoCLiente());
			ps.executeUpdate();
			ps.close();
			if (c.getTipoCLiente().equals("E")) {
				ps = conexao.prepareStatement("INSERT INTO clienteEspecial(cpfCliente,qtdDEsconto)VALUES(?,?)");
				ps.setString(1, c.getCpfCliente());
				ps.setInt(2, c.getQtdDEsconto());
				ps.executeUpdate();
			}else if(c.getTipoCLiente().equals("N")) {
				ps = conexao.prepareStatement("INSERT INTO clienteNormal(cpfCliente,descontoDinheiro)VALUES(?,?)");
				ps.setString(1, c.getCpfCliente());
				ps.setInt(2, c.getDescontoDinheiro());
				ps.executeUpdate();
			}else {
				throw new ClienteNaoEncontradoException("Tipo Cliente n�o � Especial ou Normal");
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} finally {
			ConnectionFactory.closeConnection(conexao);
			try {
				ps.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	
	public List<Cliente> read() {

        Connection conexao = ConnectionFactory.getConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Cliente> clientes = new ArrayList<>();

        try {
            ps = conexao.prepareStatement("SELECT * FROM cliente");
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCpfCliente(rs.getString("cpfCliente"));
                cliente.setNomeCLiente(rs.getString("nomeCLiente"));
                cliente.setRgCLiente(rs.getString("rgCLiente"));
                cliente.setEnderecoCliente(rs.getString("enderecoCliente"));
                cliente.setTelefoneCLiente(rs.getString("telefoneCLiente"));
                cliente.setTipoCLiente(rs.getString("tipoCLiente"));
                
                clientes.add(cliente);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
			ConnectionFactory.closeConnection(conexao);
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
        return clientes;
    }
}