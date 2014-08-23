package eu.artofcoding.emf;

import org.eclipse.emf.ecore.*;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * https://www.ibm.com/developerworks/library/os-eclipse-dynamicemf/
 */
public class PlainDynamicEmfModelTest {

    private static final String namespaceUri = "http:///com.ibm.dynamic.example.bookstore.ecore";

    private EPackage createBookStoreModel() {
        // Instantiate EcoreFactory
        final EcoreFactory eCoreFactory = EcoreFactory.eINSTANCE;
        // Instantiate EcorePackage
        final EcorePackage eCorePackage = EcorePackage.eINSTANCE;
        // Create EClass instance to model BookStore class
        final EClass bookStoreEClass = eCoreFactory.createEClass();
        bookStoreEClass.setName("BookStore");
        // Create EClass instance to model Book class
        final EClass bookEClass = eCoreFactory.createEClass();
        bookEClass.setName("Book");
        // Create attributes for BookStore class as specified in the model
        final EAttribute bookStoreOwner = eCoreFactory.createEAttribute();
        bookStoreOwner.setName("owner");
        bookStoreOwner.setEType(eCorePackage.getEString());
        final EAttribute bookStoreLocation = eCoreFactory.createEAttribute();
        bookStoreLocation.setName("location");
        bookStoreLocation.setEType(eCorePackage.getEString());
        final EReference bookStore_Books = eCoreFactory.createEReference();
        bookStore_Books.setName("books");
        bookStore_Books.setEType(bookEClass);
        bookStore_Books.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
        bookStore_Books.setContainment(true);
        // Add owner, location and books attributes/references to BookStore class
        bookStoreEClass.getEStructuralFeatures().add(bookStoreOwner);
        bookStoreEClass.getEStructuralFeatures().add(bookStoreLocation);
        bookStoreEClass.getEStructuralFeatures().add(bookStore_Books);
        // Create attributes for Book class as defined in the model
        final EAttribute bookName = eCoreFactory.createEAttribute();
        bookName.setName("name");
        bookName.setEType(eCorePackage.getEString());
        final EAttribute bookISBN = eCoreFactory.createEAttribute();
        bookISBN.setName("isbn");
        bookISBN.setEType(eCorePackage.getEInt());
        // Add name and isbn attributes to Book class
        bookEClass.getEStructuralFeatures().add(bookName);
        bookEClass.getEStructuralFeatures().add(bookISBN);
        // Instantiate EPackage and provide unique URI to identify this package
        final EPackage ePackage = eCoreFactory.createEPackage();
        ePackage.setName("BookStorePackage");
        ePackage.setNsPrefix("bookStore");
        ePackage.setNsURI(namespaceUri);
        // Place BookStore and Book classes in bookStoreEPackage
        ePackage.getEClassifiers().add(bookStoreEClass);
        ePackage.getEClassifiers().add(bookEClass);
        return ePackage;
    }

    private EObject createBookStoreInstance(EPackage ePackage) {
        final EFactory eFactory = ePackage.getEFactoryInstance();
        // Create dynamic instance of BookStoreEClass and BookEClass
        final EClass bookStoreEClass = (EClass) ePackage.getEClassifier("BookStore");
        final EObject bookStoreEObject = eFactory.create(bookStoreEClass);
        // Set the values of bookStoreObject attributes
        final EAttribute bookStoreOwner = (EAttribute) bookStoreEClass.getEStructuralFeature("owner");
        bookStoreEObject.eSet(bookStoreOwner, "David Brown");
        final EAttribute bookStoreLocation = (EAttribute) bookStoreEClass.getEStructuralFeature("location");
        bookStoreEObject.eSet(bookStoreLocation, "Street#12, Top Town, NY");
        // List with books
        final EReference booksEReference = (EReference) bookStoreEClass.getEStructuralFeature("books");
        final List books = (List) bookStoreEObject.eGet(booksEReference);
        // A book
        final EClass bookEClass = (EClass) ePackage.getEClassifier("Book");
        final EObject bookEObject = eFactory.create(bookEClass);
        final EAttribute bookName = (EAttribute) bookEClass.getEStructuralFeature("name");
        bookEObject.eSet(bookName, "Harry Potter and the Deathly Hallows");
        final EAttribute bookISBN = (EAttribute) bookEClass.getEStructuralFeature("isbn");
        bookEObject.eSet(bookISBN, 157221);
        // Add book to list
        books.add(bookEObject);
        return bookStoreEObject;
    }

    @Test
    public void createSaveAndLoadPlainEModel() {
        final long start = System.currentTimeMillis();
        // Create model
        final EPackage ePackage = createBookStoreModel();
        final EModelHelper emh = EModelHelper.eINSTANCE();
        final Path ecorePath = Paths.get("bookStore.ecore");
        emh.save(ePackage, ecorePath);
        // Create instance
        final EObject eObject = createBookStoreInstance(ePackage);
        final EInstanceHelper eih = EInstanceHelper.eINSTANCE(ePackage.getEFactoryInstance());
        final Path xmlPath = Paths.get("bookStore.xml");
        eih.save(eObject, xmlPath);
        // Load
        final EObject loadedBookStore = eih.load(namespaceUri, ePackage, xmlPath);
        // Read/get the values of bookStoreObject attributes
        final EAttribute bookStoreOwner = (EAttribute) loadedBookStore.eClass().getEStructuralFeature("owner");
        final String _bookStoreOwner = (String) loadedBookStore.eGet(bookStoreOwner);
        final EAttribute bookStoreLocation = (EAttribute) loadedBookStore.eClass().getEStructuralFeature("location");
        final String _bookStoreLocation = (String) loadedBookStore.eGet(bookStoreLocation);
        System.out.printf("BookStore: %s, %s%n", _bookStoreOwner, _bookStoreLocation);
        // Read/get the values of bookObject attributes
        final EObject loadedBook = loadedBookStore.eContents().get(0);
        final EAttribute bookName = (EAttribute) loadedBook.eClass().getEStructuralFeature("name");
        final String _bookName = (String) loadedBook.eGet(bookName);
        final EAttribute bookISBN = (EAttribute) loadedBook.eClass().getEStructuralFeature("isbn");
        final Integer _bookIsbn = (Integer) loadedBook.eGet(bookISBN);
        System.out.printf("Book: %s, %s%n", _bookName, _bookIsbn);
        final long stop = System.currentTimeMillis();
        System.out.printf("Time: %d ms%n", stop - start);
    }

}
