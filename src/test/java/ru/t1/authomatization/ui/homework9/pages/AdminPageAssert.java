package ru.t1.authomatization.ui.homework9.pages;

import com.codeborne.selenide.Condition;
import org.assertj.core.api.AbstractAssert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AdminPageAssert extends AbstractAssert<AdminPageAssert,AdminPage> {

    protected AdminPageAssert(AdminPage adminPage, Class < ? > selfType) {
        super(adminPage, selfType);
    }
    public AdminPageAssert(AdminPage actual) {
        super(actual, AdminPageAssert.class);
    }

    public AdminPageAssert checkPageVisible() {
        actual.getBackToSiteLink().shouldBe(visible);
        actual.getAdminTitle().shouldBe(visible);
        actual.getAddProductButton().shouldBe(visible);
        actual.getAddProductPrice().shouldBe(visible);
        actual.getAddProductButton().shouldBe(visible);
        return this;
    }

    public void checkAddMessageIsVisible() {
        actual.getToastContainer()
                .shouldBe(visible, Duration.ofSeconds(10))
                .should(Condition.text("Товар успешно добавлен"));
    }

    public void checkEditMessageIsVisible(long productId) {
        actual.getToastContainer()
                .shouldBe(visible, Duration.ofSeconds(10))
                .should(Condition.text("Товар #" + productId + " обновлен"));
    }
}
