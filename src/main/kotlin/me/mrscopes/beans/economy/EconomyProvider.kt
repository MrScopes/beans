package me.mrscopes.beans.economy

import me.mrscopes.beans.Beans
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.OfflinePlayer

class EconomyProvider : Economy {
    override fun isEnabled(): Boolean {
        return true
    }

    override fun getName(): String {
        return Beans.instance.name
    }

    override fun hasBankSupport(): Boolean {
        return false
    }

    override fun fractionalDigits(): Int {
        return -1
    }

    override fun format(money: Double): String {
        return "$$money"
    }

    override fun currencyNamePlural(): String {
        return name
    }

    override fun currencyNameSingular(): String {
        return name
    }

    override fun hasAccount(p0: String?): Boolean {
        TODO("Not yet implemented")
    }


    override fun hasAccount(player: OfflinePlayer?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasAccount(player: String?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasAccount(player: OfflinePlayer?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBalance(player: String?): Double {
        TODO("Not yet implemented")
    }

    override fun getBalance(player: OfflinePlayer?): Double {
        TODO("Not yet implemented")
    }

    override fun getBalance(player: String?, worldName: String?): Double {
        TODO("Not yet implemented")
    }

    override fun getBalance(player: OfflinePlayer?, worldName: String?): Double {
        TODO("Not yet implemented")
    }

    override fun has(player: String?, worldName: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(player: OfflinePlayer?, worldName: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(player: String?, worldName: String?, money: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(player: OfflinePlayer?, worldName: String?, money: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(player: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(player: OfflinePlayer?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(player: String?, worldName: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(player: OfflinePlayer?, worldName: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(player: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(player: OfflinePlayer?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(player: String?, worldName: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(player: OfflinePlayer?, worldName: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun createBank(player: String?, worldName: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun createBank(name: String, player: OfflinePlayer?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun deleteBank(player: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun bankBalance(player: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun bankHas(name: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun bankWithdraw(name: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun bankDeposit(name: String?, money: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun isBankOwner(player: String?, name: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun isBankOwner(name: String?, player: OfflinePlayer?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun isBankMember(name: String?, player: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun isBankMember(name: String?, player: OfflinePlayer?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun getBanks(): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(player: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(player: OfflinePlayer?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(player: String?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }
}