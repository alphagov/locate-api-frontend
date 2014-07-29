<#include "header.ftl">
<article>

        <h2>Locate API explorer</h2>

        <p>Use this tool to test your credentials</p>


        <p>Credentials can be requested on the <a href="/locate/create-user">create user page.</a></p>

        <hr/>

        <h2>Make an API request</h2>

        <fieldset>
            <label for="name">
                Bearer token
            </label>

            <input id="bearer_token" name="bearer_token" value="" autocomplete="off" class="text long" type="text">

            <label for="name">
                Postcode
            </label>

            <input id="postcode" name="postcode" value="" autocomplete="off" class="text long" type="text">

        </fieldset>
        <hr/>

        <input id="submit-address-search" class="button next" value="Search for address data" type="submit">
        <input id="submit-authority-search" class="button next" value="Search for authority data" type="submit">


        <h2>Results</h2>
        <div id="explorer-results" aria-live="polite"></div>

    </article>
<#include "footer.ftl">
