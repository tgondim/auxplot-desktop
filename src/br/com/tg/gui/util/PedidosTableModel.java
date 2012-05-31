package br.com.tg.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import br.com.tg.entidades.Pedido;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.StatusPedido;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class PedidosTableModel extends AbstractTableModel {

	public static final int ID_INDEX = 0;
	public static final int NOME_PESSOA_INDEX = 1;
	public static final int NOME_PROJETO_INDEX = 2;
	public static final int STATUS_PEDIDO_INDEX = 3;
	public static final int VALOR_TOTAL_INDEX = 4;

	protected String[] columnNames;
	protected Vector<Object> dataVector;
	
	public PedidosTableModel(String[] newColumnNames) {
        this.columnNames = newColumnNames;
        this.dataVector = new Vector<Object>();
	}
	
	public String getColumnNames(int column) {
		return columnNames[column];
	}

    public boolean isCellEditable(int row, int column) {
        if (column == ID_INDEX || column == NOME_PESSOA_INDEX
        		|| column == NOME_PROJETO_INDEX
        		|| column == STATUS_PEDIDO_INDEX
        		|| column == VALOR_TOTAL_INDEX) return false;
        else return true;
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case ID_INDEX:
            	return Integer.class;
            case NOME_PESSOA_INDEX:
            	return JComboBox.class;
            case NOME_PROJETO_INDEX:
            	return String.class;
            case STATUS_PEDIDO_INDEX:
            	return JComboBox.class;
            case VALOR_TOTAL_INDEX:
            	return String.class;
            default:
               return Object.class;
        }
    }
    
    @Override
    public Object getValueAt(int row, int column) {
    	Pedido pedido = new Pedido();
    	if (row >= 0) {
    		pedido = (Pedido)dataVector.get(row);
    	}
        switch (column) {
            case ID_INDEX:
            	return pedido.getId();
            case NOME_PESSOA_INDEX:
            	return pedido.getPessoaPai();
            case NOME_PROJETO_INDEX:
            	return pedido.getNomeProjeto();
            case STATUS_PEDIDO_INDEX:
            	return pedido.getStatusPedido();
            case VALOR_TOTAL_INDEX:
            	return Validador.inserirVirgula(pedido.getValorTotal());
            default:
        }
        return new Object();
    }
    
    public void setValueAt(Object value, int row, int column) {
    	Pedido pedido = (Pedido)dataVector.get(row);
        switch (column) {
        case ID_INDEX:
        	pedido.setId((Integer)value);
        	break;
        case NOME_PESSOA_INDEX:
        	pedido.setPessoaPai((Pessoa)value);
        	break;
        case NOME_PROJETO_INDEX:
        	pedido.setNomeProjeto((String)value);
        	break;
        case STATUS_PEDIDO_INDEX:
        	pedido.setStatusPedido((StatusPedido)value);
        	break;
        case VALOR_TOTAL_INDEX:
        	pedido.setValorTotal(Validador.removerVirgula((String)value));
        	break;
        default:
        	System.out.println("invalid index");
        }
        fireTableCellUpdated(row, column);
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        Pedido pedido = (Pedido)dataVector.get(dataVector.size() - 1);
        if (pedido.getPessoaPai().getNome().trim().equals("") &&
        		pedido.getNomeProjeto().trim().equals("") &&
        		pedido.getValorTotal() == 0) 
        {
           return true;
        }
        else return false;
    }

    public void addEmptyRow() {
        dataVector.add(new Pedido(true));
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }
    
    public void addRow(Pedido newPedido) {
    	dataVector.add(newPedido);
    	fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }
    
    public void removeRow(int row) {
    	dataVector.removeElementAt(row);
    }
    
    public void clearTable() {
    	dataVector = new Vector<Object>();
    }

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}
	
	@Override
	public String getColumnName(int column){  
		return columnNames[column];  
	}
	
	public boolean isEmpty() {
		if (dataVector.firstElement() == null) {
			return true;
		}
		return false;
	}
	
	public int getRow(Pedido pedido) {
		int indice = dataVector.indexOf(pedido);
		return indice;
	}
	
	public List<Pedido> getList() {
		List<Pedido> listaPedido = new ArrayList<Pedido>();
		for (Iterator<Object> i = dataVector.iterator(); i.hasNext();) {
			listaPedido.add((Pedido)i.next());
		}
		return listaPedido;
	}


}
