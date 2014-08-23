package eu.artofcoding.emf;

import org.eclipse.emf.ecore.*;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class DynamicEmfModelTest {

    private final String namespaceUri = "http:///com.ibm.dynamic.example.bookstore.ecore";

    private EPackage createBookStoreModel() {
        final EModelHelper EH = EModelHelper.eINSTANCE();
        // Create EClass instance to model BookStore class
        final EClass bookStoreEClass = EH.createEClass("BookStore");
        // Create EClass instance to model Book class
        final EClass bookEClass = EH.createEClass("Book");
        // Instantiate EcorePackage
        final EcorePackage eCorePackage = EcorePackage.eINSTANCE;
        // Create attributes for BookStore class as specified in the model
        final EAttribute bookStoreOwner = EH.createEAttribute("owner", eCorePackage.getEString());
        final EAttribute bookStoreLocation = EH.createEAttribute("location", eCorePackage.getEString());
        final EReference bookStore_Books = EH.createEReference("books", bookEClass);
        bookStore_Books.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
        bookStore_Books.setContainment(true);
        // Add owner, location and books attributes/references to BookStore class
        EH.addEStructuralFeature(bookStoreEClass, bookStoreOwner, bookStoreLocation, bookStore_Books);
        // Create attributes for Book class as defined in the model
        final EAttribute bookName = EH.createEAttribute("name", eCorePackage.getEString());
        final EAttribute bookISBN = EH.createEAttribute("isbn", eCorePackage.getEInt());
        // Add name and isbn attributes to Book class
        EH.addEStructuralFeature(bookEClass, bookName, bookISBN);
        // Instantiate EPackage and provide unique URI to identify this package
        final EPackage ePackage = EH.createEPackage("BookStorePackage", "bookStore", namespaceUri);
        // Place BookStore and Book classes in bookStoreEPackage
        EH.addEClassifier(ePackage, bookStoreEClass, bookEClass);
        return ePackage;
    }

    private EObject createBookStoreInstance(EPackage ePackage) {
        final EFactory eFactory = ePackage.getEFactoryInstance();
        final EInstanceHelper eih = EInstanceHelper.eINSTANCE(eFactory);
        // Create dynamic instance of BookStoreEClass and BookEClass
        final EClass bookStoreEClass = (EClass) ePackage.getEClassifier("BookStore");
        final EObject bookStoreEObject = eih.createEObject(ePackage, "BookStore");
        // Set the values of bookStoreObject attributes
        eih.createEAttribute(bookStoreEObject, "owner", "David Brown");
        eih.createEAttribute(bookStoreEObject, "location", "Street#12, Top Town, NY");
        // List with books
        final EReference booksEReference = (EReference) bookStoreEClass.getEStructuralFeature("books");
        final List books = (List) bookStoreEObject.eGet(booksEReference);
        // A book
        final EObject bookEObject = eih.createEObject(ePackage, "Book");
        eih.createEAttribute(bookEObject, "name", "Harry Potter and the Deathly Hallows");
        eih.createEAttribute(bookEObject, "isbn", 12345);
        // Add book to list
        books.add(bookEObject);
        return bookStoreEObject;
    }

    @Test
    public void createSaveAndLoadEModel() {
        final long start = System.currentTimeMillis();
        // Create model
        final EPackage ePackage = createBookStoreModel();
        final EModelHelper emh = EModelHelper.eINSTANCE();
        emh.save(ePackage, Paths.get("bookStore.ecore"));
        // Create instance
        final EInstanceHelper eih = EInstanceHelper.eINSTANCE(ePackage.getEFactoryInstance());
        final EObject eObject = createBookStoreInstance(ePackage);
        eih.save(eObject, Paths.get("bookStore.xml"));
        // Load
        final EObject loadedBookStore = eih.load(namespaceUri, ePackage, Paths.get("bookStore.xml"));
        // Read/Get the values of bookStoreObject attributes
        final String bookStoreOwner = eih.getEAttributeValue(loadedBookStore, "owner", String.class);
        final String bookStoreLocation = eih.getEAttributeValue(loadedBookStore, "location", String.class);
        System.out.printf("BookStore: %s, %s%n", bookStoreOwner, bookStoreLocation);
        // Read/Get the values of bookObject attributes
        final EObject loadedBook = loadedBookStore.eContents().get(0);
        final String bookName = eih.getEAttributeValue(loadedBook, "name", String.class);
        final Integer bookIsbn = eih.getEAttributeValue(loadedBook, "isbn", Integer.class);
        System.out.printf("Book: %s, %s%n", bookName, bookIsbn);
        final long stop = System.currentTimeMillis();
        System.out.printf("Time: %d ms%n", stop - start);
    }

}
