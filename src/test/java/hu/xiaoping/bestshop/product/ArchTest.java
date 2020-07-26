package hu.xiaoping.bestshop.product;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("hu.xiaoping.bestshop.product");

        noClasses()
            .that()
                .resideInAnyPackage("hu.xiaoping.bestshop.product.service..")
            .or()
                .resideInAnyPackage("hu.xiaoping.bestshop.product.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..hu.xiaoping.bestshop.product.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
