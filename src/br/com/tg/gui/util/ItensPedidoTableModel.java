package br.com.tg.gui.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import br.com.tg.entidades.ItemPedido;
import br.com.tg.entidades.Plotagem;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class ItensPedidoTableModel extends AbstractTableModel {

	public static final int PLOTAGEM_INDEX = 0;
	public static final int PLANTA_DESCRICAO_INDEX = 1;
	public static final int QUANTIDADE_INDEX = 2;
	public static final int VALOR_UNITARIO_INDEX = 3;
	public static final int VALOR_TOTAL_INDEX = 4;

	protected String[] columnNames;
	protected Vector<Object> dataVector;
	
	public ItensPedidoTableModel(String[] newColumnNames) {
        this.columnNames = newColumnNames;
        this.dataVector = new Vector<Object>();
	}
	
	public String getColumnNames(int column) {
		return columnNames[column];
	}

    public boolean isCellEditable(int row, int column) {
    	//implementar a permissao do usuario
    	//if ((column == VALOR_UNITARIO_INDEX) && (rotinadoUsuario)) return false;
        if (column == VALOR_TOTAL_INDEX) return false;
        else return true;
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case PLOTAGEM_INDEX:
            	return JComboBox.class;
            case PLANTA_DESCRICAO_INDEX:
            	return String.class;
            case QUANTIDADE_INDEX:
            	return String.class;
            case VALOR_UNITARIO_INDEX:
            	return String.class;
            case VALOR_TOTAL_INDEX:
            	return String.class;
            default:
               return Object.class;
        }
    }
    
    @Override
    public Object getValueAt(int row, int column) {
    	ItemPedido itemPedido = new ItemPedido();
        if (row >= 0 && row < dataVector.size()) {
        	itemPedido = (ItemPedido)dataVector.get(row);
        }
        switch (column) {
            case PLOTAGEM_INDEX:
            	return itemPedido.getPlotagem();
            case PLANTA_DESCRICAO_INDEX:
            	return itemPedido.getPlantaDescricao();
            case QUANTIDADE_INDEX:
            	return Validador.inserirVirgula(itemPedido.getQuantidade());
            case VALOR_UNITARIO_INDEX:
            	return Validador.inserirVirgula(itemPedido.getValorUnitario());
            case VALOR_TOTAL_INDEX:
            	return Validador.inserirVirgula(itemPedido.getValorTotal());
            default:
        }
        return new Object();
    }
    
    public void setValueAt(Object value, int row, int column) {
    	if (row >= 0 && row < dataVector.size()) {
    		ItemPedido itemPedido = (ItemPedido)dataVector.get(row);
    		switch (column) {
    		case PLOTAGEM_INDEX:
    			itemPedido.setPlotagem((Plotagem)value);
    			break;
    		case PLANTA_DESCRICAO_INDEX:
    			itemPedido.setPlantaDescricao((String)value);
    			break;
    		case QUANTIDADE_INDEX:
    			itemPedido.setQuantidade(Validador.removerVirgula((String)value));
    			break;
    		case VALOR_UNITARIO_INDEX:
    			itemPedido.setValorUnitario(Validador.removerVirgula((String)value));
    			break;
    		case VALOR_TOTAL_INDEX:
    			itemPedido.setValorTotal(Validador.removerVirgula((String)value));
    			break;
    		default:
    			System.out.println("invalid index");
    		}
    		fireTableCellUpdated(row, column);
    	}
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        ItemPedido itemPedido = (ItemPedido)dataVector.get(dataVector.size() - 1);
        if (itemPedido.getPlotagem().getDescricao().trim().equals("") &&
        		itemPedido.getPlantaDescricao().trim().equals("") &&
        		itemPedido.getQuantidade() == 0 &&
        		itemPedido.getValorUnitario() == 0) 
        {
           return true;
        }
        else return false;
    }

    public void addEmptyRow() {
        dataVector.add(new ItemPedido());
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }
    
    public void addRow(ItemPedido newItemPedido) {
    	dataVector.add(newItemPedido);
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
	
	public int getRow(ItemPedido itemPedido) {
		int indice = dataVector.indexOf(itemPedido);
		return indice;
	}
	
	public Set<ItemPedido> getList() {
		Set<ItemPedido> listaItemPedido = new HashSet<ItemPedido>();
		for (Iterator<Object> i = dataVector.iterator(); i.hasNext();) {
			listaItemPedido.add((ItemPedido)i.next());
		}
		return listaItemPedido;
	}


}
