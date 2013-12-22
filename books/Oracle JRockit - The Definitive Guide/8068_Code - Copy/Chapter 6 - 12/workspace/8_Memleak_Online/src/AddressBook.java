
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class AddressBook extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3241064526563032620L;

	private static class Contact {
		private final String name;
		private final String phoneNumber;

		public Contact(String name, String phoneNumber) {
			this.name = name;
			this.phoneNumber = phoneNumber;
		}

		public String getName() {
			return name;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}
	}

	private static class ContactList {
		private List<Contact> contacts;
		private Map<String, Contact> nameToContact;
		private Map<String, Contact> numberToContact;

		public ContactList() {
			contacts = new ArrayList<Contact>();
			nameToContact = new HashMap<String, Contact>();
			numberToContact = new HashMap<String, Contact>();
			populateList();
		}

		private void populateList() {
			addContact(new Contact("Animal Control", "555-X-TERM-N-8"));
			addContact(new Contact("Debby Freeman", "555-0141"));
			addContact(new Contact("Ghostbusters", "555-2368"));
			addContact(new Contact("Grandma", "555-0110"));
			addContact(new Contact("Jenny", "867-5309"));
			addContact(new Contact("Lisa Martin", "555-7247"));
			addContact(new Contact("The White House", "202-456-1414"));
			addContact(new Contact("Tony Banks", "555-GEN-ESIS"));
		}

		public boolean addContact(Contact contact) {
			if (!nameToContact.containsKey(contact.getName())) {
				contacts.add(contact);
				nameToContact.put(contact.getName(), contact);
				numberToContact.put(contact.getPhoneNumber(), contact);
				return true;
			} else {
				return false;
			}
		}

		public boolean changeContact(String name, Contact newContact) {
			if (nameToContact.containsKey(name)
					&& (newContact.getName().equals(name) || !nameToContact
							.containsKey(newContact.getName()))) {
				removeContact(name);
				addContact(newContact);
				return true;
			} else {
				return false;
			}
		}

		public boolean removeContact(String name) {
			Contact contact = nameToContact.get(name);
			if (contact != null) {
				contacts.remove(contact);
//				numberToContact.remove(contact.getPhoneNumber());
//				nameToContact.remove(contact.getName());
				return true;
			} else {
				return false;
			}
		}

		public int size() {
			return contacts.size();
		}

		public Contact getContactByIndex(int index) {
			return contacts.get(index);
		}

		public Contact getContactByName(String name) {
			return nameToContact.get(name);
		}

		public Contact getContactByNumber(String number) {
			return numberToContact.get(number);
		}
	}

	private static final int PAD = 5;

	private static final String ADD = "Add";
	private static final String CHANGE = "Change";
	private static final String REMOVE = "Remove";
	private static final String LOOKUP_NAME = "Lookup name";
	private static final String LOOKUP_NUMBER = "Lookup number";

	private AbstractTableModel tableModel;
	private ContactList contactList;

	private JTable table;

	public AddressBook() {
		super("MemoryLeaker");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(Box.createVerticalStrut(PAD), BorderLayout.PAGE_START);
		contentPane
				.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_START);
		contentPane.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_END);
		contentPane.add(new JScrollPane(createTable()), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, PAD, PAD));
		buttonPanel.add(createButton(ADD));
		buttonPanel.add(createButton(CHANGE));
		buttonPanel.add(createButton(REMOVE));
		buttonPanel.add(createButton(LOOKUP_NAME));
		buttonPanel.add(createButton(LOOKUP_NUMBER));
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
	}

	private Component createTable() {
		contactList = new ContactList();
		tableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 3752205748599276328L;

			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return contactList.size();
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				Contact c = contactList.getContactByIndex(rowIndex);
				return (columnIndex == 0) ? c.getName() : c.getPhoneNumber();
			}
		};
		TableColumnModel tableColumnModel = new DefaultTableColumnModel();
		TableColumn tableColumn = new TableColumn(0);
		tableColumn.setHeaderValue("Name");
		tableColumnModel.addColumn(tableColumn);
		tableColumn = new TableColumn(1);
		tableColumn.setHeaderValue("Phone number");
		tableColumnModel.addColumn(tableColumn);
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table = new JTable(tableModel, tableColumnModel, listSelectionModel);
		return table;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.addActionListener(this);
		return button;
	}

	private ContactDialog createContactDialog(String name, String phoneNumber) {
		ContactDialog contactDialog = new ContactDialog(this, "Add contact");
		contactDialog.setContactName(name);
		contactDialog.setPhoneNumber(phoneNumber);
		return contactDialog;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals(ADD)) {
			addAction();
		} else if (action.equals(CHANGE)) {
			changeAction();
		} else if (action.equals(REMOVE)) {
			removeAction();
		} else if (action.equals(LOOKUP_NAME)) {
			lookupNameAction();
		} else if (action.equals(LOOKUP_NUMBER)) {
			lookupNumberAction();
		}
	}

	private void addAction() {
		ContactDialog addDialog = createContactDialog("", "");
		if (addDialog.showDialog() == ContactDialog.OK) {
			if (contactList.addContact(new Contact(addDialog.getContactName(),
					addDialog.getPhoneNumber()))) {
				int insertedIndex = contactList.size() - 1;
				tableModel.fireTableRowsInserted(insertedIndex, insertedIndex);
			}
		}
	}

	private void changeAction() {
		int index = table.getSelectedRow();
		Contact contact = contactList.getContactByIndex(index);
		ContactDialog changeDialog = createContactDialog(contact.getName(),
				contact.getPhoneNumber());
		if (changeDialog.showDialog() == ContactDialog.OK) {
			if (contactList.changeContact(contact.getName(), new Contact(
					changeDialog.getContactName(), changeDialog
							.getPhoneNumber()))) {
				int insertedIndex = contactList.size() - 1;
				tableModel.fireTableRowsDeleted(index, index);
				tableModel.fireTableRowsInserted(insertedIndex, insertedIndex);
			}
		}
	}

	private void removeAction() {
		int [] indexArray = table.getSelectedRows();
		ArrayList<Contact> contactsToDelete = new ArrayList<Contact>();
		int min = Integer.MAX_VALUE;
		int max = 0;
		for (int i = 0; i < indexArray.length; i++) {
			contactsToDelete.add(contactList.getContactByIndex(indexArray[i]));
			min = Math.min(min, indexArray[i]);
			max = Math.max(max, indexArray[i]);
		}
		
		for (Contact c : contactsToDelete) {
			contactList.removeContact(c.getName());
			
		}		
	
		tableModel.fireTableRowsDeleted(min, max);
	}

	private void lookupNameAction() {
		final JDialog lookupDialog = new JDialog(this, "Lookup name", true);
		lookupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = lookupDialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane
				.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_START);
		contentPane.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_END);

		JPanel componentPanel = new JPanel();
		componentPanel.setLayout(new BoxLayout(componentPanel,
				BoxLayout.PAGE_AXIS));
		componentPanel.add(Box.createVerticalStrut(PAD));
		JLabel label = new JLabel("Name:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(label);
		componentPanel.add(Box.createVerticalStrut(PAD));
		final JTextField nameField = new JTextField(30);
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(nameField);
		componentPanel.add(Box.createVerticalStrut(PAD));
		label = new JLabel("Number:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(label);
		componentPanel.add(Box.createVerticalStrut(PAD));
		final JTextField numberField = new JTextField(30);
		numberField.setAlignmentX(Component.LEFT_ALIGNMENT);
		numberField.setEditable(false);
		numberField.setFocusable(false);
		componentPanel.add(numberField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, PAD, PAD));
		JButton button = new JButton("Lookup name");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Contact c = contactList.getContactByName(nameField.getText());
				numberField.setText((c != null) ? c.getPhoneNumber() : "");
			}
		});
		buttonPanel.add(button);
		button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lookupDialog.setVisible(false);
				lookupDialog.dispose();
			}
		});
		buttonPanel.add(button);
		componentPanel.add(buttonPanel, BorderLayout.PAGE_END);

		contentPane.add(componentPanel, BorderLayout.CENTER);
		lookupDialog.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = lookupDialog.getSize();
		lookupDialog.setLocation(screenSize.width / 2 - size.width / 2,
				screenSize.height / 2 - size.height / 2);
		lookupDialog.setVisible(true);
	}

	private void lookupNumberAction() {
		final JDialog lookupDialog = new JDialog(this, "Lookup name", true);
		lookupDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = lookupDialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane
				.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_START);
		contentPane.add(Box.createHorizontalStrut(PAD), BorderLayout.LINE_END);

		JPanel componentPanel = new JPanel();
		componentPanel.setLayout(new BoxLayout(componentPanel,
				BoxLayout.PAGE_AXIS));
		componentPanel.add(Box.createVerticalStrut(PAD));
		JLabel label = new JLabel("Number:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(label);
		componentPanel.add(Box.createVerticalStrut(PAD));
		final JTextField numberField = new JTextField(30);
		numberField.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(numberField);
		componentPanel.add(Box.createVerticalStrut(PAD));
		label = new JLabel("Name:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		componentPanel.add(label);
		componentPanel.add(Box.createVerticalStrut(PAD));
		final JTextField nameField = new JTextField(30);
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setEditable(false);
		nameField.setFocusable(false);
		componentPanel.add(nameField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, PAD, PAD));
		JButton button = new JButton("Lookup number");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Contact c = contactList.getContactByNumber(numberField
						.getText());
				nameField.setText((c != null) ? c.getName() : "");
			}
		});
		buttonPanel.add(button);
		button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lookupDialog.setVisible(false);
				lookupDialog.dispose();
			}
		});
		buttonPanel.add(button);
		componentPanel.add(buttonPanel, BorderLayout.PAGE_END);

		contentPane.add(componentPanel, BorderLayout.CENTER);
		lookupDialog.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = lookupDialog.getSize();
		lookupDialog.setLocation(screenSize.width / 2 - size.width / 2,
				screenSize.height / 2 - size.height / 2);
		lookupDialog.setVisible(true);
	}

	public static void main(String[] args) {
		AddressBook ml = new AddressBook();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		ml.setSize(screenSize.width / 2, screenSize.height / 2);
		ml.setLocation(screenSize.width / 4, screenSize.height / 4);
		ml.setVisible(true);
	}

	private static class ContactDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = -5217000742825348006L;
		private static final String CANCEL_LABEL = "CANCEL";
		private static final String OK_LABEL = "OK";
		public static final int OK = 1;
		public static final int CANCEL = 0;
		private JTextField nameField;
		private JTextField phoneNumerField;
		private boolean ok;

		public ContactDialog(JFrame parent, String title) {
			super(parent, title, true);
			nameField = new JTextField(30);
			nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
			phoneNumerField = new JTextField(30);
			phoneNumerField.setAlignmentX(Component.LEFT_ALIGNMENT);

			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(Box.createHorizontalStrut(PAD),
					BorderLayout.LINE_START);
			contentPane.add(Box.createHorizontalStrut(PAD),
					BorderLayout.LINE_END);
			JPanel componentPanel = new JPanel();
			componentPanel.setLayout(new BoxLayout(componentPanel,
					BoxLayout.PAGE_AXIS));
			componentPanel.add(Box.createVerticalStrut(PAD));
			componentPanel.add(createLabel("Name:"));
			componentPanel.add(Box.createVerticalStrut(PAD));
			componentPanel.add(nameField);
			componentPanel.add(Box.createVerticalStrut(PAD));
			componentPanel.add(createLabel("Phone number:"));
			componentPanel.add(Box.createVerticalStrut(PAD));
			componentPanel.add(phoneNumerField);

			JPanel buttonPanel = new JPanel();
			buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			buttonPanel
					.setLayout(new FlowLayout(FlowLayout.TRAILING, PAD, PAD));
			buttonPanel.add(createButton(OK_LABEL));
			buttonPanel.add(createButton(CANCEL_LABEL));
			componentPanel.add(buttonPanel, BorderLayout.PAGE_END);

			contentPane.add(componentPanel, BorderLayout.CENTER);

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}

		private JLabel createLabel(String text) {
			JLabel label = new JLabel(text);
			label.setAlignmentX(Component.LEFT_ALIGNMENT);
			return label;
		}

		private JButton createButton(String text) {
			JButton button = new JButton(text);
			button.addActionListener(this);
			return button;
		}

		public int showDialog() {
			pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size = getSize();
			setLocation(screenSize.width / 2 - size.width / 2,
					screenSize.height / 2 - size.height / 2);
			setVisible(true);
			return (ok) ? OK : CANCEL;
		}

		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if (action.equals(OK_LABEL)) {
				ok = true;
				setVisible(false);
				dispose();
			} else if (action.equals(CANCEL_LABEL)) {
				setVisible(false);
				dispose();
			}
		}

		public String getContactName() {
			return nameField.getText();
		}

		public void setContactName(String name) {
			nameField.setText(name);
		}

		public String getPhoneNumber() {
			return phoneNumerField.getText();
		}

		public void setPhoneNumber(String phoneNumber) {
			phoneNumerField.setText(phoneNumber);
		}
	}
}
