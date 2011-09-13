/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.cdo.releng.doc.article;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Diagram</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.cdo.releng.doc.article.Diagram#getCode <em>Code</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.cdo.releng.doc.article.ArticlePackage#getDiagram()
 * @model
 * @generated
 */
public interface Diagram extends BodyElement
{
  /**
   * Returns the value of the '<em><b>Code</b></em>' attribute. <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Code</em>' attribute isn't clear, there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Code</em>' attribute.
   * @see #setCode(String)
   * @see org.eclipse.emf.cdo.releng.doc.article.ArticlePackage#getDiagram_Code()
   * @model required="true"
   * @generated
   */
  String getCode();

  /**
   * Sets the value of the '{@link org.eclipse.emf.cdo.releng.doc.article.Diagram#getCode <em>Code</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @param value
   *          the new value of the '<em>Code</em>' attribute.
   * @see #getCode()
   * @generated
   */
  void setCode(String value);

} // Diagram
