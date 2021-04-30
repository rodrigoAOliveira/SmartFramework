package com.arcthos.arcthosmart.shared.activity

import android.os.Bundle
import android.view.KeyEvent
import com.salesforce.androidsdk.ui.SalesforceActivityDelegate
import com.salesforce.androidsdk.ui.SalesforceActivityInterface
import dagger.android.support.DaggerAppCompatActivity

abstract class SalesforceDaggerActivity : DaggerAppCompatActivity(), SalesforceActivityInterface {

    private val delegate: SalesforceActivityDelegate = SalesforceActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate()
    }

    public override fun onResume() {
        super.onResume()
        delegate.onResume(true)
    }

    override fun onUserInteraction() {
        delegate.onUserInteraction()
    }

    public override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    public override fun onDestroy() {
        delegate.onDestroy()
        super.onDestroy()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return delegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event)
    }

    override fun onLogoutComplete() {

    }

    override fun onUserSwitched() {
        delegate.onResume(true)
    }
}